package br.com.fiap.varresp.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ManipuladorDeExcecoes {

    private static final Logger LOG = LoggerFactory.getLogger(ManipuladorDeExcecoes.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> recursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> rotaNaoEncontrada(NoResourceFoundException ex) {
        return montar(HttpStatus.NOT_FOUND, "Recurso não encontrado", null);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResposta> conflito(ConflitoException ex) {
        return montar(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResposta> integridade(DataIntegrityViolationException ex) {
        LOG.warn("Violação de integridade", ex);
        return montar(HttpStatus.CONFLICT, "A operação viola uma regra de integridade dos dados", null);
    }

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<ErroResposta> requisicaoInvalida(RequisicaoInvalidaException ex) {
        return montar(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> validacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos", campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> corpoIlegivel(HttpMessageNotReadableException ex) {
        return montar(HttpStatus.BAD_REQUEST, "Corpo da requisição ausente ou malformado", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return montar(HttpStatus.BAD_REQUEST, "Valor inválido para o parâmetro " + ex.getName(), null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResposta> parametroAusente(MissingServletRequestParameterException ex) {
        return montar(HttpStatus.BAD_REQUEST, "Parâmetro obrigatório ausente: " + ex.getParameterName(), null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> metodoNaoSuportado(HttpRequestMethodNotSupportedException ex) {
        return montar(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não suportado para esta rota", null);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroResposta> tipoDeMidiaNaoSuportado(HttpMediaTypeNotSupportedException ex) {
        return montar(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Use o tipo de conteúdo application/json", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> erroInterno(Exception ex) {
        LOG.error("Erro interno não tratado", ex);
        return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", null);
    }

    private ResponseEntity<ErroResposta> montar(HttpStatus status, String mensagem, Map<String, String> campos) {
        ErroResposta corpo = new ErroResposta(Instant.now(), status.value(), status.getReasonPhrase(), mensagem, campos);
        return ResponseEntity.status(status).body(corpo);
    }
}
