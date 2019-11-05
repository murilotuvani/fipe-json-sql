/**
 *  @see https://mit-license.org/
 *  The MIT License (MIT)
 * Copyright © 2019 <copyright holders>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: The above copyright
 * notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.cartola.fipe;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

/**
 * 04/11/2019 15:39:26
 *
 * @author murilo
 */
public class Converter {

    private static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static String server = "http://veiculos.fipe.org.br/api/veiculos/";

    public static void main(String args[]) {
        try {
            // http://veiculos.fipe.org.br/api/veiculos/ConsultarTabelaDeReferencia
            //String path = "ConsultarMarcas";
            String path = "ConsultarTabelaDeReferencia";
            
            Converter conv = new Converter();
//            PostMethod method = conv.getRequest(path);
//
            HttpClient client = new HttpClient();
//            int response = client.executeMethod(method);
//            System.out.println("Response : " + response);
//            ByteArrayOutputStream baos = conv.getResponseBody(method);
//
//            String responseBody = baos.toString("UTF-8");
//            System.out.println(responseBody);
//            conv.save(path, responseBody);
            
            path = "ConsultarMarcas";
            int tabela = 248;
            int tipoVeiculo = 3;
            String body = "{\n"
                    + "  \"codigoTabelaReferencia\": " + tabela + ",\n"
                    + "  \"codigoTipoVeiculo\": " + tipoVeiculo + "\n"
                    + "}";
            PostMethod method = conv.getRequest(path, body);
            int response = client.executeMethod(method);
            System.out.println("Response : " + response);
            ByteArrayOutputStream baos = conv.getResponseBody(method);

            String responseBody = baos.toString("UTF-8");
            System.out.println(responseBody);
            conv.save(path + "-" + tabela + "-" + tipoVeiculo + "-", responseBody);

        } catch (IOException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ByteArrayOutputStream getResponseBody(HttpMethod method) throws IOException {
        InputStream input = method.getResponseBodyAsStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(input);
        int aByte;
        while ((aByte = bis.read()) != -1) {
            baos.write(aByte);
        }
        baos.flush();
        baos.close();
        bis.close();
        return baos;
    }

    private void save(String path, String responseBody) throws IOException {
        LocalDateTime ldt = LocalDateTime.now();
        String fileName = path + ldt.format(DATETIME_FORMATTER) + ".json";
        File file = new File(fileName);
        System.out.println("Arquivo : " + file.getAbsolutePath());
        Files.write(file.toPath(), responseBody.getBytes(), StandardOpenOption.CREATE_NEW);
    }

    private PostMethod getRequest(String path) {
        PostMethod method = new PostMethod(server + path);
        method.addRequestHeader("Host", "veiculos.fipe.org.br");
        method.addRequestHeader("Referer", "http://veiculos.fipe.org.br");
        method.addRequestHeader("Content-Type", "application/json");

        return method;
    }

    private PostMethod getRequest(String path, String body) throws UnsupportedEncodingException {
        PostMethod method = getRequest(path);
        
        RequestEntity requestEntity = new StringRequestEntity(body, "application/json", "UTF-8");
        method.setRequestEntity(requestEntity);
        return method;
    }
}
