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
package net.cartola.fipe.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * 05/11/2019 14:18:23
 * @author murilo
 */
public class Marca implements Serializable {
    
    private transient int tabelaId;
    private transient int marcaId;
    @SerializedName(value = "Value")
    private String value;
    @SerializedName(value = "Label")
    private String label;
    @SerializedName(value = "Modelos")
    private List<Modelo> modelos;

    public int getTabelaId() {
        return tabelaId;
    }

    public void setTabelaId(int tabelaId) {
        this.tabelaId = tabelaId;
    }

    public int getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(int marcaId) {
        this.marcaId = marcaId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Modelo> getModelos() {
        return modelos;
    }

    public void setModelos(List<Modelo> modelos) {
        this.modelos = modelos;
    }

    @Override
    public String toString() {
        return "Marca{value=" + value + ", label=" + label + '}';
    }

}
