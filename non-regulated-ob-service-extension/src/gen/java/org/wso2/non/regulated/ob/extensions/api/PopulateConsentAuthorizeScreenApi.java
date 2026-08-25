package org.wso2.non.regulated.ob.extensions.api;

import org.wso2.non.regulated.ob.extensions.impl.PopulateConsentAuthorizeScreenApiImpl;
import org.wso2.non.regulated.ob.extensions.model.ErrorResponse;
import org.wso2.non.regulated.ob.extensions.model.PopulateConsentAuthorizeScreenRequestBody;
import org.wso2.non.regulated.ob.extensions.model.Response200ForPopulateConsentAuthorizeScreen;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

import javax.validation.Valid;

/**
* Represents a collection of functions to interact with the API endpoints.
*/
@Path("/populate-consent-authorize-screen")
@Api(description = "the populate-consent-authorize-screen API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2026-07-23T14:19:20.447074+05:30[Asia/Colombo]", comments = "Generator version: 7.24.0")
public class PopulateConsentAuthorizeScreenApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "handle validations before consent  authorization and consent data to load in consent authorization UI", notes = "", response = Response200ForPopulateConsentAuthorizeScreen.class, authorizations = {
        @Authorization(value = "OAuth2", scopes = {
             }),
        
        @Authorization(value = "BasicAuth")
         }, tags={ "Consent" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Ok", response = Response200ForPopulateConsentAuthorizeScreen.class),
        @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse.class),
        @ApiResponse(code = 500, message = "Server Error", response = ErrorResponse.class)
    })
    public Response populateConsentAuthorizeScreenPost(@Valid PopulateConsentAuthorizeScreenRequestBody populateConsentAuthorizeScreenRequestBody) {

        return PopulateConsentAuthorizeScreenApiImpl.handlePopulateConsentAuthorizeScreen(populateConsentAuthorizeScreenRequestBody);
    }
}
