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
package net.cartola.fipe.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.cartola.fipe.model.Modelo;

/**
 * 05/11/2019 17:55:35
 *
 * @author murilo
 */
public class ModeloDao extends Dao<Modelo> {

    @Override
    public String getInsert() {
        //return "INSERT INTO modelos (modelo_id,marca_id,tabela_id,value,label) VALUES (?,?,?,?,?)";
        return "INSERT INTO modelos (marca_id,tabela_id,value,label) VALUES (?,?,?,?)";
    }

    @Override
    public String getUpdate() {
        return "UPDATE modelos SET modelo_id=?,marca_id=?,tabela_id=?,value=?,label=?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, Modelo m) throws SQLException {
        int idx = 1;
//stmt.setInt(idx++, m.getModeloId());
        stmt.setInt(idx++, m.getMarcaId());
        stmt.setInt(idx++, m.getTabelaId());
        setNullSafe(stmt, m.getValue(), idx++);
        stmt.setString(idx++, m.getLabel());
    }

}
