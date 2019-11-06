/**
 *  @see https://mit-license.org/
 *  The MIT License (MIT)
 * Copyright © 2019 <copyright holders>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.cartola.fipe.dao.AnoModeloDao;
import net.cartola.fipe.dao.MarcaDao;
import net.cartola.fipe.dao.ModeloDao;
import net.cartola.fipe.model.AnoModelo;
import net.cartola.fipe.model.Marca;
import net.cartola.fipe.model.Modelo;
import net.cartola.fipe.model.Tabela;
import net.cartola.fipe.model.VeiculoTipo;
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

    private GsonBuilder gsonBuilder = new GsonBuilder();
    private Gson gson = gsonBuilder.create();
    private HttpClient client = new HttpClient();
    private static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static String server = "http://veiculos.fipe.org.br/api/veiculos/";

    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        try {
            // http://veiculos.fipe.org.br/api/veiculos/ConsultarTabelaDeReferencia
            //String path = "ConsultarMarcas";
//            String path = "ConsultarTabelaDeReferencia";

            int tabelaId = 1;
            Tabela tabela = new Tabela();
            tabela.setTabelaId(tabelaId);
            tabela.setCodigo(248);
            tabela.setMes("novembro/2019");
            Converter conv = new Converter();
            for (VeiculoTipo tipo : VeiculoTipo.values()) {
                List<Marca> marcas = conv.marcas(tipo, tabela);
                for (Marca marca : marcas) {
                    MarcaDao marcaDao = new MarcaDao();
                    int marcaId = marcaDao.salvar(marca);
                    marca.setMarcaId(marcaId);

                    Marca modelosDaMarca = conv.modelos(marca);
                    modelosDaMarca.setMarcaId(marca.getMarcaId());
                    modelosDaMarca.setLabel(marca.getLabel());
                    modelosDaMarca.setValue(marca.getValue());

                    for (Modelo modelo : modelosDaMarca.getModelos()) {
                        ModeloDao modeloDao = new ModeloDao();
                        modelo.setTabelaId(tabelaId);
                        modelo.setMarca(marca);
                        modelo.setMarcaId(marcaId);
                        
                        int modeloId = modeloDao.salvar(modelo);
                        modelo.setModeloId(modeloId);
                        
                        List<AnoModelo> anos = conv.anosModelos(modelo);
                        AnoModeloDao amd = new AnoModeloDao();
                        for (AnoModelo an : anos) {

//                        anos.forEach(an -> {
                            an.setTabelaId(tabelaId);
                            an.setMarcaId(marcaId);
                            an.setModeloId(modeloId);
                            amd.salvar(an);
//                        });
                        }
                    }

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void tabelas() throws IOException {
        String path = "ConsultarTabelaDeReferencia";

        Converter conv = new Converter();
        PostMethod method = conv.getRequest(path);
        int response = client.executeMethod(method);
        System.out.println("Response : " + response);
        ByteArrayOutputStream baos = conv.getResponseBody(method);

        String responseBody = baos.toString("UTF-8");
        System.out.println(responseBody);
        save(path, responseBody);
    }

    private List<Marca> marcas(VeiculoTipo veiculoTipo, final Tabela tabela) throws UnsupportedEncodingException, IOException {
        String path = "ConsultarMarcas";
        int tabelaCodigo = tabela.getCodigo();
        String body = "{\n"
                + "  \"codigoTabelaReferencia\": " + tabelaCodigo + ",\n"
                + "  \"codigoTipoVeiculo\": " + veiculoTipo.getCodigo() + "\n"
                + "}";
        PostMethod method = getRequest(path, body);
        int response = client.executeMethod(method);
        System.out.println("Response : " + response);
        ByteArrayOutputStream baos = getResponseBody(method);

        String responseBody = baos.toString("UTF-8");
        System.out.println(responseBody);
        save(path + "-" + tabelaCodigo + "-" + veiculoTipo.getCodigo() + "-", responseBody);

        Marca[] marcas = gson.fromJson(responseBody, Marca[].class);
        List<Marca> marcasL = Arrays.asList(marcas);
        marcasL.forEach(m -> {
            m.setTabela(tabela);
            m.setVeiculoTipo(veiculoTipo);
            m.setTabelaId(tabela.getTabelaId());
        });
        return marcasL;
    }

    private Marca modelos(Marca marca) throws UnsupportedEncodingException, IOException {
        String path = "ConsultarModelos";
        int tabelaCodigo = marca.getTabela().getCodigo();
        int tipoVeiculo = marca.getVeiculoTipo().getCodigo();
        String codigoMarca = marca.getValue();
        String body = "{\n"
                + "  \"codigoTabelaReferencia\": " + tabelaCodigo + ",\n"
                + "  \"codigoTipoVeiculo\": " + tipoVeiculo + ",\n"
                + "  \"codigoMarca\": " + codigoMarca + "\n"
                + "}";
        PostMethod method = getRequest(path, body);
        int response = client.executeMethod(method);
        System.out.println("Response : " + response);
        ByteArrayOutputStream baos = getResponseBody(method);

        String responseBody = baos.toString("UTF-8");
        System.out.println(responseBody);
        save(path + "-" + tabelaCodigo + "-" + tipoVeiculo + "-" + codigoMarca + "-", responseBody);

        Marca modelosDaMarca = gson.fromJson(responseBody, Marca.class);
        return modelosDaMarca;
    }

    private List<AnoModelo> anosModelos(Modelo modelo) throws UnsupportedEncodingException, IOException {
        int tabelaCodigo = modelo.getMarca().getTabela().getCodigo();
        int veiculoTipo = modelo.getMarca().getVeiculoTipo().getCodigo();
        String marcaCodigo = modelo.getMarca().getValue();
        String modeloCodigo = modelo.getValue();
        ;
        String path = "ConsultarAnoModelo";
        String body = "{\n"
                + "  \"codigoTabelaReferencia\": " + tabelaCodigo + ",\n"
                + "  \"codigoTipoVeiculo\": " + veiculoTipo + ",\n"
                + "  \"codigoMarca\": " + marcaCodigo + ",\n"
                + "  \"codigoModelo\": " + modeloCodigo + "\n"
                + "}";
        PostMethod method = getRequest(path, body);
        int response = client.executeMethod(method);
        System.out.println("Response : " + response);
        ByteArrayOutputStream baos = getResponseBody(method);

        String responseBody = baos.toString("UTF-8");
        System.out.println(responseBody);
        save(path + "-" + tabelaCodigo + "-" + veiculoTipo + "-" + marcaCodigo + "-" + modeloCodigo + "-", responseBody);
        
        AnoModelo[] anosModelo = gson.fromJson(responseBody, AnoModelo[].class);
        return Arrays.asList(anosModelo);
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
