package com.sap.cloud.sdk;

import com.google.gson.Gson;

import com.sap.cloud.sdk.services.recastai.RecastWebhookResponseWrapper;
import com.sap.cloud.sdk.services.recastai.botresponses.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ChatServlet.class);

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {

        logger.info("Start get method: " + request.getRequestURI());
        String parameter = request.getParameter("name");
        logger.info("Get parameter 'name': " + parameter);

        TextResponse textResponse = new TextResponse("入力：" + parameter);

        List<CardContent> cardContents = new ArrayList<>();
        List<Button> buttons = new ArrayList<>(); //Dummy

        CardContent cardContent = new CardContent();
        cardContent.setTitle("タイトル");
        cardContent.setSubtitle("サブタイトル");
        cardContent.setButtons(buttons);
        cardContents.add(cardContent);

        ListContent listContent = new ListContent();
        listContent.setElements(cardContents);
        ListResponse listResponse = new ListResponse(listContent);

        RecastWebhookResponseWrapper webhookResponse = new RecastWebhookResponseWrapper(textResponse, listResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(webhookResponse.getResponse());
    }
}
