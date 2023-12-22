package com.Trello.Tests;

import com.Trello.Api.TrelloApiClass;
import com.Trello.Model.TrelloCard;
import com.Trello.Utilities.ConfigurationReader;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Properties;
import java.util.Random;

public class TrelloApiTest {

    private static TrelloApiClass trelloApi;
    private static String boardId;
    private static String listId;
    private static String cardId1;
    private static String cardId2;

    @BeforeSuite
    public void setup() {
        Properties properties = ConfigurationReader.initializeProperties();
        System.out.println("Loaded properties: " + properties);

        String apiKey = ConfigurationReader.getProperties().getProperty("key");
        String token = ConfigurationReader.getProperties().getProperty("token");

        trelloApi = new TrelloApiClass(apiKey, token);
    }

    @Test(priority = 1)
    public void createBoardTest() {
        Response boardCreationResponse = trelloApi.createBoard("Trello My Board");
        boardId = boardCreationResponse.then().statusCode(200).extract().path("id");
    }

    @Test(priority = 2)
    public void createListTest() {
        listId = trelloApi.createList(boardId, "New List").then().statusCode(200).extract().path("id");
    }

    @Test(priority = 3)
    public void createCardsTest() {
        Assert.assertNotNull(listId, "ListId is null");

        TrelloCard card1 = new TrelloCard("Card Title 1", "Card description 1");
        TrelloCard card2 = new TrelloCard("Card Title 2", "Card description 2");

        Response response1 = trelloApi.createCard(listId, card1);
        Response response2 = trelloApi.createCard(listId, card2);
        
        System.out.println("Response 1: " + response1.then().log().all().extract().asString());
        System.out.println("Response 2: " + response2.then().log().all().extract().asString());

        cardId1 = response1.then().log().all().statusCode(200).extract().path("id");
        cardId2 = response2.then().log().all().statusCode(200).extract().path("id");

        Assert.assertNotNull(cardId1, "CardId1 is null");
        Assert.assertNotNull(cardId2, "CardId2 is null");
    }



    @Test(priority = 4)
    public void updateCardTest() {
        Random random = new Random();
        int temp = random.nextInt(2);
        
        if (temp == 0 && cardId1 != null) {
            trelloApi.updateCard(cardId1, "New Card Title", "Card description edited.")
                    .then().statusCode(200);
        } else if (temp == 1 && cardId2 != null) {
            trelloApi.updateCard(cardId2, "New Card Title", "Card description edited.")
                    .then().statusCode(200);
        } else {
            System.out.println("No card available for update.");
        }
    }

    @Test(priority = 5)
    public void deleteCardsTest() {
        if (cardId1 != null) {
            trelloApi.deleteCard(cardId1);
            System.out.println("Deleted card with ID: " + cardId1);
        }

        if (cardId2 != null) {
            trelloApi.deleteCard(cardId2);
            System.out.println("Deleted card with ID: " + cardId2);
        }
    }
    
    @Test(priority = 0)
    public void cleanupTest() {
        cleanup();
    }

    @AfterSuite
    public void cleanup() {
        try {
            deleteCardsTest();
        } catch (Exception e) {
            System.out.println("Error deleting cards: " + e.getMessage());
        }

        if (boardId != null) {
            try {
                trelloApi.deleteBoard(boardId);
                System.out.println("Deleted board with ID: " + boardId);
            } catch (Exception e) {
                System.out.println("Error deleting board: " + e.getMessage());
            }
        } else {
            System.out.println("BoardId is null. Skipped board deletion.");
        }

        System.out.println("ended");
    }

}
