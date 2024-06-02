package lambdaapp;

import appdata.Customer;
import appdata.RequestData;
import appdata.ResponseData;
import appdata.DataStore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppFunctions implements RequestHandler<RequestData, ResponseData> {

    private static final Logger logger = LogManager.getLogger(AppFunctions.class);

    @Override
    public ResponseData handleRequest(RequestData theRequest, Context context) {
        DataStore theData = new DataStore();

        int returnCode = 0;
        String message = null;
        Customer returnedCustomer = null;

        switch (theRequest.getActionRequested().toLowerCase()) {
            case "find": {
                returnedCustomer = theData.getACustomer(theRequest.getCustomerId());
                if (returnedCustomer == null) {
                    logger.error("Customer with ID: " + theRequest.getCustomerId() + " not found.");
                    message = "Customer with ID: " + theRequest.getCustomerId() + " not found.";
                    returnCode = 404;
                } else {
                    returnCode = 0;
                    message = "Customer Id: " + theRequest.getCustomerId() + " found!";
                }
                break;
            }
            case "chgname": {
                returnedCustomer = theData.getACustomer(theRequest.getCustomerId());
                if (returnedCustomer == null) {
                    logger.error("Customer with ID: " + theRequest.getCustomerId() + " not found.");
                    message = "Customer with ID: " + theRequest.getCustomerId() + " not found.";
                    returnCode = 404;
                } else {
                    returnedCustomer.setCustomerName(theRequest.getPayload());
                    message = "Customer Id: " + returnedCustomer.getCustomerID() + "'s name has been changed to: " + returnedCustomer.getCustomerName() + "!";
                }
                break;
            }
            case "add": {
                String[] newData = theRequest.getPayload().split(",");
                if (newData.length < 1 || newData[0].trim().isEmpty()) {
                    logger.error("Insufficient data to add a new customer.");
                    message = "Insufficient data to add a new customer.";
                    returnCode = 422;
                } else {
                    returnedCustomer = new Customer();
                    if (newData.length > 0) { returnedCustomer.setCustomerName(newData[0].trim()); }
                    if (newData.length > 1) { returnedCustomer.setCustomerStreetAddress(newData[1].trim()); }
                    if (newData.length > 2) { returnedCustomer.setCustomerCity(newData[2].trim()); }
                    if (newData.length > 3) { returnedCustomer.setCustomerState(newData[3].trim()); }
                    if (newData.length > 4) { returnedCustomer.setCustomerZip(newData[4].trim()); }

                    message = "Customer Name: " + returnedCustomer.getCustomerName() + " has been added!";
                }
                break;
            }
            default: {
                logger.error("Invalid action: " + theRequest.getActionRequested());
                message = "Invalid action: " + theRequest.getActionRequested();
                returnCode = 400;
            }
        }

        return new ResponseData(returnCode, returnedCustomer, message);
    }
}


