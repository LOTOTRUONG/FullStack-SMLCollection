package fr.loto.util;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        // Handle different types of exceptions here
        if (exception instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid request: " + exception.getMessage())
                    .build();
        } else if (exception instanceof UnauthorizedAccessException) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Unauthorized access: " + exception.getMessage())
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Internal server error: " + exception.getMessage())
                    .build();
        }
    }
}
