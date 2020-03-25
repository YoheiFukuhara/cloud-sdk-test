package com.sap.cloud.sdk;

import com.google.gson.Gson;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.DefaultErpHttpDestination;
import com.sap.cloud.sdk.s4hana.connectivity.ErpHttpDestination;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.Customer;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/customer")
public class GetCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(GetCustomerServlet.class);
    private static final ErpHttpDestination destination =
            DestinationAccessor.getDestination("Erp1809")
                    .asHttp().decorate(DefaultErpHttpDestination::new);

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {

        logger.info("Start get method: " + request.getRequestURI());
        String parameter = request.getParameter("name");
        logger.info("Get parameter 'name': " + parameter);

        try{
            final String searchParameter = "substringof(CustomerName, '" + parameter + "')";

            final List<Customer> customers =
                    new DefaultBusinessPartnerService()
                            .getAllCustomer()
                            .withQueryParameter("$filter", searchParameter)
                            .select(Customer.CUSTOMER, Customer.CUSTOMER_FULL_NAME)
                            .top(5)
                            .execute(destination);
            logger.info(new Gson().toJson(customers));
            response.setContentType("application/json");
            response.getWriter().write(new Gson().toJson(customers));

        } catch (final ODataException e){
            logger.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
        }
    }
}
