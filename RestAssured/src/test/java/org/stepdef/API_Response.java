package org.stepdef;
import lombok.Builder;
import lombok.Getter;
import org.json.simple.JSONObject;

@Builder
@Getter
public class API_Response {
    private final long statusCode;
    private final String responseStatusLine;
    private final long responseTime;
    private final String header;
    private final String contentType;
    private final String server;
    private final String contentEncoding;
    private final String jsonStr;
    private final JSONObject jsonBody;
    //private final Map<String, Object> jsonBody;

       @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("\n");
            sb.append("Response : ").append("\n");
           sb.append("StatusCode :" ).append(statusCode).append("\n");
           sb.append("StatusLine :").append(responseStatusLine).append("\n");
           sb.append("ResponseTime :").append(responseTime).append("\n");
           //sb.append("Header : ").append(header).append("\n");
           sb.append("Content-Type : ").append(contentType).append("\n");
           sb.append("Server : ").append(server).append("\n");
           sb.append("Content-Ending :").append(contentEncoding).append("\n");
           sb.append("Body :  ").append(jsonStr);
           return sb.toString();
        }
}
