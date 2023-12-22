package com.Trello.Api;

import com.Trello.Model.TrelloCard;
import com.Trello.Utilities.ConfigurationReader;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TrelloApiClass {
    private final String apiKey;
    private final String token;
    private final String baseUrl;

    public TrelloApiClass(String apiKey, String token) {
        this.apiKey = apiKey;
        this.token = token;
        this.baseUrl = ConfigurationReader.getProperties().getProperty("baseURI");
    }

    public Response createBoard(String boardName) {
        return RestAssured.given()
                .contentType("application/json")
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .queryParam("name", boardName)
                .post(baseUrl + "/boards");
    }

    public Response createList(String boardId, String listName) {
        return RestAssured.given()
                .contentType("application/json")
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .queryParam("name", listName)
                .post(baseUrl + "/boards/" + boardId + "/lists");
    }

    public Response createCard(String listId, TrelloCard card) {
        return RestAssured.given()
                .contentType("text/plain")
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .queryParam("name", card.getName())
                .queryParam("desc", card.getDesc())
                .post(baseUrl + "/cards");
    }

    public Response updateCard(String cardId, String newName, String newDesc) {
        return RestAssured.given()
                .contentType("application/json")
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .queryParam("name", newName)
                .queryParam("desc", newDesc)
                .put(baseUrl + "/cards/" + cardId);
    }

    public void deleteCard(String cardId) {
        RestAssured.given()
                .contentType("application/json")
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .delete(baseUrl + "/cards/" + cardId);
    }

    public void deleteBoard(String boardId) {
        RestAssured.given()
                .contentType("application/json")
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .delete(baseUrl + "/boards/" + boardId);
    }
}
