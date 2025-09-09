package br.com.dio.controller;

import br.com.dio.dto.BoardDetailsDTO;
import br.com.dio.dto.CardDetailsDTO;
import br.com.dio.persistence.entity.BoardEntity;
import br.com.dio.persistence.entity.BoardColumnEntity;
import br.com.dio.persistence.entity.BoardColumnKindEnum;
import br.com.dio.persistence.entity.CardEntity;
import br.com.dio.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static br.com.dio.persistence.config.ConnectionConfig.getConnection;

public class BoardController {
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // CORS + Static files
        server.createContext("/", new StaticFileHandler());
        
        // API endpoints
        server.createContext("/api/boards", new BoardHandler());
        server.createContext("/api/boards/", new BoardDetailsHandler());
        server.createContext("/api/cards", new CardHandler());
        server.createContext("/api/cards/", new CardDetailsHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor iniciado em http://vmlinuxd:8080");
    }
    
    private void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }
    
    private void sendJsonResponse(HttpExchange exchange, Object data, int statusCode) throws IOException {
        setCorsHeaders(exchange);
        String json = mapper.writeValueAsString(data);
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
    
    private void sendErrorResponse(HttpExchange exchange, String message, int statusCode) throws IOException {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        sendJsonResponse(exchange, error, statusCode);
    }
    
    class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }

            String filePath = "/static/index2.html"; 

            try (InputStream inputStream = getClass().getResourceAsStream(filePath)) {
                if (inputStream == null) {
                    String notFoundMsg = "Arquivo não encontrado: " + filePath;
                    byte[] notFoundResponse = notFoundMsg.getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(404, notFoundResponse.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(notFoundResponse);
                    }
                    return;
                }

                String htmlContent = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));

                byte[] response = htmlContent.getBytes(StandardCharsets.UTF_8);
                
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendErrorResponse(exchange, "Erro interno do servidor.", 500);
            }
        }
    }
    
    class BoardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            try {
                switch (exchange.getRequestMethod()) {
                    case "POST" -> createBoard(exchange);
                    case "DELETE" -> deleteBoard(exchange);
                    default -> sendErrorResponse(exchange, "Método não suportado", 405);
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, e.getMessage(), 500);
            }
        }
        
        private void createBoard(HttpExchange exchange) throws IOException, SQLException {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, Object> request = mapper.readValue(body, Map.class);
            
            BoardEntity entity = new BoardEntity();
            entity.setName((String) request.get("name"));
            
            // Criar colunas padrão
            entity.getBoardColumns().add(createColumn("A Fazer", BoardColumnKindEnum.INITIAL, 0));
            entity.getBoardColumns().add(createColumn("Fazendo", BoardColumnKindEnum.PENDING, 1));
            entity.getBoardColumns().add(createColumn("Concluído", BoardColumnKindEnum.FINAL, 2));
            entity.getBoardColumns().add(createColumn("Cancelado", BoardColumnKindEnum.CANCEL, 3));
            
            try (var connection = getConnection()) {
                new BoardService(connection).insert(entity);
                sendJsonResponse(exchange, entity, 201);
            }
        }
        
        private void deleteBoard(HttpExchange exchange) throws IOException, SQLException {
            String path = exchange.getRequestURI().getPath();
            Long id = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));
            
            try (var connection = getConnection()) {
                boolean deleted = new BoardService(connection).delete(id);
                if (deleted) {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Board excluído com sucesso");
                    sendJsonResponse(exchange, response, 200);
                } else {
                    sendErrorResponse(exchange, "Board não encontrado", 404);
                }
            }
        }
    }
    
    class BoardDetailsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, "Método não suportado", 405);
                return;
            }
            
            try {
                String path = exchange.getRequestURI().getPath();
                Long id = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));
                
                try (var connection = getConnection()) {
                    var optional = new BoardQueryService(connection).showBoardDetails(id);
                    if (optional.isPresent()) {
                        sendJsonResponse(exchange, optional.get(), 200);
                    } else {
                        sendErrorResponse(exchange, "Board não encontrado", 404);
                    }
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, e.getMessage(), 500);
            }
        }
    }
    
    class CardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            try {
                switch (exchange.getRequestMethod()) {
                    case "POST" -> createCard(exchange);
                    case "PUT" -> updateCard(exchange);
                    default -> sendErrorResponse(exchange, "Método não suportado", 405);
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, e.getMessage(), 500);
            }
        }
        
        private void createCard(HttpExchange exchange) throws IOException, SQLException {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, Object> request = mapper.readValue(body, Map.class);
            
            CardEntity entity = new CardEntity();
            entity.setTitle((String) request.get("title"));
            entity.setDescription((String) request.get("description"));
            
            Long boardId = Long.parseLong(request.get("boardId").toString());
            
            try (var connection = getConnection()) {
                var board = new BoardQueryService(connection).findById(boardId)
                    .orElseThrow(() -> new RuntimeException("Board não encontrado"));
                entity.setBoardColumn(board.getInitialColumn());
                
                new CardService(connection).create(entity);
                sendJsonResponse(exchange, entity, 201);
            }
        }
        
        private void updateCard(HttpExchange exchange) throws IOException, SQLException {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, Object> request = mapper.readValue(body, Map.class);
            
            Long cardId = Long.parseLong(request.get("cardId").toString());
            String action = (String) request.get("action");
            
            try (var connection = getConnection()) {
                var service = new CardService(connection);
                
                switch (action) {
                    case "move" -> {
                        Long boardId = Long.parseLong(request.get("boardId").toString());
                        var board = new BoardQueryService(connection).findById(boardId)
                            .orElseThrow(() -> new RuntimeException("Board não encontrado"));
                        var boardColumnsInfo = board.getBoardColumns().stream()
                            .map(bc -> new br.com.dio.dto.BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                            .toList();
                        service.moveToNextColumn(cardId, boardColumnsInfo);
                    }
                    case "block" -> {
                        String reason = (String) request.get("reason");
                        Long boardId = Long.parseLong(request.get("boardId").toString());
                        var board = new BoardQueryService(connection).findById(boardId)
                            .orElseThrow(() -> new RuntimeException("Board não encontrado"));
                        var boardColumnsInfo = board.getBoardColumns().stream()
                            .map(bc -> new br.com.dio.dto.BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                            .toList();
                        service.block(cardId, reason, boardColumnsInfo);
                    }
                    case "unblock" -> {
                        String reason = (String) request.get("reason");
                        service.unblock(cardId, reason);
                    }
                    case "cancel" -> {
                        Long boardId = Long.parseLong(request.get("boardId").toString());
                        var board = new BoardQueryService(connection).findById(boardId)
                            .orElseThrow(() -> new RuntimeException("Board não encontrado"));
                        var cancelColumn = board.getCancelColumn();
                        var boardColumnsInfo = board.getBoardColumns().stream()
                            .map(bc -> new br.com.dio.dto.BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                            .toList();
                        service.cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
                    }
                }
                
                Map<String, String> response = new HashMap<>();
                response.put("message", "Ação executada com sucesso");
                sendJsonResponse(exchange, response, 200);
            }
        }
    }
    
    class CardDetailsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1);
                return;
            }
            
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendErrorResponse(exchange, "Método não suportado", 405);
                return;
            }
            
            try {
                String path = exchange.getRequestURI().getPath();
                Long id = Long.parseLong(path.substring(path.lastIndexOf('/') + 1));
                
                try (var connection = getConnection()) {
                    var optional = new CardQueryService(connection).findById(id);
                    if (optional.isPresent()) {
                        sendJsonResponse(exchange, optional.get(), 200);
                    } else {
                        sendErrorResponse(exchange, "Card não encontrado", 404);
                    }
                }
            } catch (Exception e) {
                sendErrorResponse(exchange, e.getMessage(), 500);
            }
        }
    }
    
    private BoardColumnEntity createColumn(String name, BoardColumnKindEnum kind, int order) {
        var column = new BoardColumnEntity();
        column.setName(name);
        column.setKind(kind);
        column.setOrder(order);
        return column;
    }
}