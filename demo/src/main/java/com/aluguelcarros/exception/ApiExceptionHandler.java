package com.aluguelcarros.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces
@Singleton
public class ApiExceptionHandler implements ExceptionHandler<RuntimeException, HttpResponse<JsonError>> {

    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Override
    public HttpResponse<JsonError> handle(HttpRequest request, RuntimeException exception) {
        if (exception instanceof RecursoException) {
            return HttpResponse.notFound(new JsonError(exception.getMessage()));
        }

        if (exception instanceof IllegalArgumentException) {
            return HttpResponse.badRequest(new JsonError(exception.getMessage()));
        }

        if (exception instanceof AcessoNegadoException) {
            return HttpResponse.status(io.micronaut.http.HttpStatus.FORBIDDEN)
                    .body(new JsonError(exception.getMessage()));
        }

        LOG.error("Erro não tratado em {}", request.getPath(), exception);
        return HttpResponse.serverError(new JsonError("Erro interno no servidor."));
    }
}