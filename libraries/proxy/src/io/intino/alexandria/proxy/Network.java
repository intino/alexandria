package io.intino.alexandria.proxy;

import io.intino.alexandria.logger.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Network {
  private List<String> cookies;
  private String referer = "";
  private String location;
  private Integer responseCode;
  private String userAgent;
  private String acceptLanguage;
  private ArrayList<NameValuePair> headers = null;
  private Header[] lastHeaders = null;

  public static class NetworkException extends Exception {
    NetworkException(String message) {
      super(message);
    }
    NetworkException(Throwable cause) {
      super(cause);
    }
  }

  public Network() {
    location = "";
    responseCode = 0;
    cookies = new ArrayList<>();
    userAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0";
    acceptLanguage = "en-US,en;q=0.5";
  }

  public byte[] sendPostString(String url, String postParams) throws NetworkException {
    try {
      return sendPost(url, postParams, null);
    } catch (Exception e) {
      throw new NetworkException(e);
    }
  }

  @SuppressWarnings("unused")
  public byte[] sendPostString(String url, String postParams, ArrayList<NameValuePair> files) throws NetworkException {
    try {
      return sendPost(url, postParams, files);
    } catch (Exception e) {
      throw new NetworkException(e);
    }
  }

  private byte[] sendPost(String url, String postParams, ArrayList<NameValuePair> files) throws NetworkException {
    lastHeaders = null;
    SSLContextBuilder builder = new SSLContextBuilder();
    try {
      builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
      CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();

      HttpPost post = new HttpPost(url);

      addHeaders(post);

      if (headers != null)
        for (NameValuePair header : headers) {
          post.addHeader(header.getName(), header.getValue());
        }

      ArrayList<NameValuePair> parameters = convertQueryToArrayList(postParams);
      if (files == null)
        post.setEntity(new UrlEncodedFormEntity(parameters));
      else {
        MultipartEntityBuilder builderEntity = MultipartEntityBuilder.create();

        for (NameValuePair parameter : parameters) {
          builderEntity.addTextBody(parameter.getName(), parameter.getValue());
        }

        for (NameValuePair file : files) {
          builderEntity.addPart(file.getName(), new FileBody(new File(file.getValue())));
        }

        post.setEntity(builderEntity.build());
      }

      HttpResponse httpResp = executeSend(client, post);
      lastHeaders = httpResp.getAllHeaders();

      Logger.debug("Sending 'POST' request to URL : " + url);
      Logger.debug("Response Code : " + responseCode);
      if (httpResp.getHeaders("Content-Type").length > 0)
        Logger.debug(httpResp.getHeaders("Content-Type")[0].getName() + ": " + httpResp.getHeaders("Content-Type")[0].getValue());
      else
        Logger.debug("Content-Type: none");

      Boolean redirect = isRedirect(responseCode, url);
      if (redirect)
        return this.sendGet(location);
      else {
        return IOUtils.toByteArray(httpResp.getEntity().getContent());
      }
    } catch (Exception e) {
      throw new NetworkException(e);
    }
  }

  public byte[] sendGetString(String url) throws NetworkException {
    return sendGet(url);
  }

  private byte[] sendGet(String url) throws NetworkException {
    lastHeaders = null;
    try {
      SSLContextBuilder builder = new SSLContextBuilder();
      builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());

      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

      RequestConfig globalConfig = RequestConfig.custom()
              .setCookieSpec(CookieSpecs.DEFAULT)
              .build();

      CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(globalConfig).build();
      RequestConfig localConfig = RequestConfig.copy(globalConfig).setCookieSpec(CookieSpecs.STANDARD).build();

      HttpGet get = new HttpGet(url);
      get.setConfig(localConfig);

      addHeaders(get);

      HttpResponse httpResp = executeSend(client, get);
      lastHeaders = httpResp.getAllHeaders();

      Logger.debug("Sending 'GET' request to URL : " + url);
      Logger.debug("Response Code : " + responseCode);
      if (httpResp.getHeaders("Content-Type").length > 0)
        Logger.debug(httpResp.getHeaders("Content-Type")[0].getName() + ": " + httpResp.getHeaders("Content-Type")[0].getValue());
      else
        Logger.debug("Content-Type: none");

      if (responseCode > 400) {
        String message = String.format("Can't download file %s, Response code: %d, Message: %s", url, responseCode, IOUtils.toString(httpResp.getEntity().getContent()));
        Logger.error(message);
        throw new NetworkException(message);
      }

      Boolean redirect = isRedirect(responseCode, url);
      if (redirect)
        return this.sendGet(location);
      else {
        return IOUtils.toByteArray(httpResp.getEntity().getContent());
      }

    } catch (Exception e) {
      throw new NetworkException(e);
    }
  }

  @SuppressWarnings("unused")
  public void download(String url, String fileName) throws IOException {
    URL website = new URL(url);
    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
    FileOutputStream fos = new FileOutputStream(fileName);
    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
  }

  @SuppressWarnings("unused")
  public String download(String url) throws NetworkException {
    return new String(sendGet(url));
  }

  @SuppressWarnings("unused")
  public byte[] downloadBytes(String url) throws NetworkException {
    return sendGet(url);
  }

  private ArrayList<NameValuePair> convertQueryToArrayList(String query) throws UnsupportedEncodingException {
    ArrayList<NameValuePair> result;

    result = new ArrayList<>();
    String[] parameters = query.split("&");
    for (String parameter : parameters) {
      String[] param = parameter.split("=");
      String name = param[0];
      String value = "";
      if (param.length > 1) value = decode(param[1], "UTF-8");

      result.add(new BasicNameValuePair(name, value));
    }

    return result;
  }

  private String getCookiesAsString() {
    String result;
    String cookie;
    StringBuilder cookiesBuffer = new StringBuilder();
    for (String cookieFull : cookies) {
      cookie = cookieFull.substring(0, cookieFull.indexOf(";") + 1);
      cookiesBuffer.append(cookie).append(" ");
    }
    result = cookiesBuffer.toString();
    if (result.length() > 0) result = result.substring(0, result.length() - 2);
    return result;
  }

  @SuppressWarnings("unused")
  public void addCookie(String name, String value) {
    cookies.add(name + "=" + value + ";");
  }

  private void setCookies(Header[] headers) {
    for (Header h : headers) {
      String cookieFull = h.getValue();
      removeCookie(getCookieName(cookieFull));
      cookies.add(cookieFull);
    }
  }

  private void removeCookie(String name) {
    List<String> newcookies = new ArrayList<>();

    for (String cookie : cookies) {
      if (!name.equals(getCookieName(cookie)))
        newcookies.add(cookie);
    }

    cookies = newcookies;
  }

  private String getCookieName(String cookieFull) {
    String[] fields = cookieFull.split(";\\s*")[0].split("=");
    return fields[0];
  }

  @SuppressWarnings("unused")
  public String getCookieValue(String name) {
    String result = "";
    for (String cookie : cookies) {
      String[] fields = cookie.split(";\\s*")[0].split("=");
      if (name.equals(fields[0]))
        result = fields[1];
    }
    return result;
  }

  @SuppressWarnings("unused")
  public List<String> getCookies() {
    return cookies;
  }

  @SuppressWarnings("unused")
  public Integer getLastResultCode() {
    return responseCode;
  }

  @SuppressWarnings("unused")
  public void setReferer(String referer) {
    this.referer = referer;
  }

  @SuppressWarnings("unused")
  public void setAgent(String agent) {
    userAgent = agent;
  }

  @SuppressWarnings("unused")
  public String getRemoteIP() {
    try {
      return new String(sendGetString("http://wtfismyip.com/text"), UTF_8);
    } catch (Exception e) {
      Logger.debug(e.getMessage());
      return "";
    }
  }

  @SuppressWarnings("unused")
  public String getDomainIP(String domain) throws UnknownHostException {
    InetAddress address = InetAddress.getByName(domain);
    return address.getHostAddress();
  }

  @SuppressWarnings("unused")
  public void setAdditionalHeaders(ArrayList<NameValuePair> headers) {
    this.headers = headers;
  }

  public Header[] getLastHeaders() {
    return lastHeaders;
  }

  private void addHeaders(HttpRequestBase entity) {
    entity.addHeader("User-Agent", userAgent);
    entity.addHeader("Accept-Language", acceptLanguage);
    entity.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    entity.addHeader("Connection", "keep-alive");
    entity.addHeader("Cookie", getCookiesAsString());
    entity.addHeader("referer", referer);
  }

  private Boolean isRedirect(Integer responseCode, String url) {
    if (responseCode < 300)
      referer = url;

    boolean redirect = false;
    if (responseCode != HttpURLConnection.HTTP_OK) {
      if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
          || responseCode == HttpURLConnection.HTTP_MOVED_PERM
          || responseCode == HttpURLConnection.HTTP_SEE_OTHER)
        redirect = true;
    }
    return redirect;
  }

  private HttpResponse executeSend(CloseableHttpClient client, HttpRequestBase entity) throws IOException {
    HttpResponse httpResp = client.execute(entity);
    responseCode = httpResp.getStatusLine().getStatusCode();

    if (httpResp.getHeaders("Set-Cookie").length > 0)
      setCookies(httpResp.getHeaders("Set-Cookie"));
    location = "";
    if (httpResp.getHeaders("Location").length > 0)
      location = httpResp.getHeaders("Location")[0].getValue();
    return httpResp;
  }
}
