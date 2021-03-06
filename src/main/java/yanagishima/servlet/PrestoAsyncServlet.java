package yanagishima.servlet;

import static java.lang.String.format;
import static yanagishima.util.AccessControlUtil.sendForbiddenError;
import static yanagishima.util.AccessControlUtil.validateDatasource;
import static yanagishima.util.HttpRequestUtil.getRequiredParameter;
import static yanagishima.util.JsonUtil.writeJSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.prestosql.client.ClientException;
import yanagishima.config.YanagishimaConfig;
import yanagishima.service.PrestoService;

@Singleton
public class PrestoAsyncServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(PrestoAsyncServlet.class);
    private static final long serialVersionUID = 1L;

    private final PrestoService prestoService;
    private final YanagishimaConfig config;

    @Inject
    public PrestoAsyncServlet(PrestoService prestoService, YanagishimaConfig config) {
        this.prestoService = prestoService;
        this.config = config;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> responseBody = new HashMap<>();
        String query = request.getParameter("query");
        if (query == null) {
            writeJSON(response, responseBody);
            return;
        }

        try {
            String user = getUsername(request);
            Optional<String> prestoUser = Optional.ofNullable(request.getParameter("user"));
            Optional<String> prestoPassword = Optional.ofNullable(request.getParameter("password"));
            if (config.isUserRequired() && user == null) {
                sendForbiddenError(response);
                return;
            }
            String datasource = getRequiredParameter(request, "datasource");
            if (config.isCheckDatasource() && !validateDatasource(request, datasource)) {
                sendForbiddenError(response);
                return;
            }
            if (user != null) {
                LOG.info(format("%s executed %s in %s", user, query, datasource));
            }
            if (prestoUser.isPresent() && prestoPassword.isPresent() && prestoUser.get().isEmpty()) {
                responseBody.put("error", "user is empty");
                writeJSON(response, responseBody);
                return;
            }
            try {
                Optional<String> sessionPropertyOptional = Optional.ofNullable(request.getParameter("session_property"));
                String queryId = executeQuery(datasource, query, sessionPropertyOptional, user, prestoUser, prestoPassword);
                responseBody.put("queryid", queryId);
            } catch (ClientException e) {
                if (prestoUser.isPresent()) {
                    LOG.error(format("%s failed to be authenticated", prestoUser.get()));
                }
                LOG.error(e.getMessage(), e);
                responseBody.put("error", e.getMessage());
            } catch (Throwable e) {
                LOG.error(e.getMessage(), e);
                responseBody.put("error", e.getMessage());
            }
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
            responseBody.put("error", e.getMessage());
        }
        writeJSON(response, responseBody);
    }

    private String executeQuery(String datasource, String query, Optional<String> sessionPropertyOptional, String user, Optional<String> prestoUser, Optional<String> prestoPassword) {
        return prestoService.doQueryAsync(datasource, query, sessionPropertyOptional, user, prestoUser, prestoPassword);
    }

    private String getUsername(HttpServletRequest request) {
        if (config.isUseAuditHttpHeaderName()) {
            return request.getHeader(config.getAuditHttpHeaderName());
        }
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        if (user != null && password != null) {
            return user;
        }
        return null;
    }
}
