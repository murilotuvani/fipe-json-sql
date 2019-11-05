/**
 *  @see https://mit-license.org/
 *  The MIT License (MIT)
 * Copyright © 2019 <copyright holders>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a  copy 
 * of this software and associated documentation files (the Software"), to deal
 * in the Software without restriction, including without limitation the  rights 
 * to use, copy, modify, merge, publish,  distribute,  sublicense,  and/or  sell 
 * copies of the Software, and  to  permit  persons  to  whom  the  Software  is 
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall  be  included  in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY  KIND,  EXPRESS  OR
 * IMPLIED, INCLUDING BUT NOT LIMITED  TO  THE  WARRANTIES  OF  MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND  NONINFRINGEMENT.  IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE  LIABLE  FOR  ANY  CLAIM,  DAMAGES  OR  OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.cartola.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import net.cartola.fipe.model.Marca;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * 05/11/2019 12:07:59
 * @author murilo
 */
public class MarcaParse {

    @Test
    public void test() throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        File file = new File("ConsultarMarcas-248-1-20191105111317.json");

        assertTrue(file.exists());

        FileReader fileReader = new FileReader(file);
        JsonReader jsonReader = gson.newJsonReader(fileReader);
        Marca[] tabelas = gson.fromJson(jsonReader, Marca[].class);

        assertNotNull(tabelas);
        System.out.println(tabelas.length);
        for (Marca tabela : tabelas) {
            System.out.println(tabela);
        }
    }
}
