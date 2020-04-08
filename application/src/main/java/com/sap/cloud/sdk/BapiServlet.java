package com.sap.cloud.sdk;

import com.google.gson.Gson;

import com.sap.cloud.sdk.cloudplatform.connectivity.*;
import com.sap.cloud.sdk.s4hana.connectivity.exception.RequestExecutionException;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RfmRequest;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.RfmRequestResult;
import com.sap.cloud.sdk.s4hana.connectivity.rfc.exception.RemoteFunctionException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.conn.jco.JCoException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/bapi")
public class BapiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(BapiServlet.class);
    private static final Destination destinationRfc =
            DestinationAccessor.getDestination("Erp1809rfc");
//    private JCoDestination jCoDestination;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {

        logger.info("Start get method: " + request.getRequestURI());
        Iterable names = destinationRfc.getPropertyNames();
        logger.info(new Gson().toJson(names));

/*
        try {
            this.jCoDestination = JCoDestinationManager.getDestination("Erp1809rfc");
        } catch (JCoException var4) {

        }
*/
        try {
            final RfmRequestResult rfmTest = new RfmRequest("RFCPING")
                    .execute(destinationRfc);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(rfmTest));
            } catch (RequestExecutionException e) {
            e.printStackTrace();
        }
    }
}
