/**
 *  @see https://mit-license.org/
 *  The MIT License (MIT)
 * Copyright © 2019 <copyright holders>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software"), to deal
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
package net.cartola.dao;

import java.sql.SQLException;
import net.cartola.fipe.dao.AnoModeloDao;
import net.cartola.fipe.dao.MarcaDao;
import net.cartola.fipe.dao.ModeloDao;
import net.cartola.fipe.dao.TabelaDao;
import net.cartola.fipe.model.AnoModelo;
import net.cartola.fipe.model.Marca;
import net.cartola.fipe.model.Modelo;
import net.cartola.fipe.model.Tabela;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * 05/11/2019 18:20:49
 *
 * @author murilo
 */
public class DaoTest {

    @Test
    public void test() throws SQLException, ClassNotFoundException {
        TabelaDao td = new TabelaDao();
        Tabela t = new Tabela();
        t.setMes("cesar/2099");
        t.setCodigo(9999);
        int tabelaId = td.salvar(t);
        assertTrue(tabelaId > 0);
        
        Marca m = new Marca();
        m.setTabelaId(tabelaId);
        m.setLabel("VW");
        m.setValue("1");
        MarcaDao md = new MarcaDao();
        int marcaId = md.salvar(m);
        assertTrue(marcaId > 0);
        
        Modelo mo = new Modelo();
        mo.setTabelaId(tabelaId);
        mo.setMarcaId(marcaId);
        mo.setLabel("Fusca");
        mo.setValue("1");
        ModeloDao mod = new ModeloDao();
        int modeloId = mod.salvar(mo);
        
        AnoModelo am = new AnoModelo();
        am.setTabelaId(tabelaId);
        am.setMarcaId(marcaId);
        am.setModeloId(modeloId);
        am.setLabel("1985 Gasolina");
        am.setValue("200");
        AnoModeloDao amd = new AnoModeloDao();
        amd.salvar(am);
    }

}
