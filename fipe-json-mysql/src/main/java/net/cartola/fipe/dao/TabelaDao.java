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
import net.cartola.fipe.model.Tabela;

/**
 * 05/11/2019 17:54:53
 *
 * @author murilo
 */
public class TabelaDao extends Dao<Tabela> {

    @Override
    public String getInsert() {
        return "INSERT INTO tabelas (codigo,mes) VALUES (?,?)";
    }

    @Override
    public String getUpdate() {
        return "UPDATE tabelas SET tabela_id=?,codigo=?,mes=?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, Tabela t) throws SQLException {
        int idx = 1;
        stmt.setInt(idx++, t.getCodigo());
        stmt.setString(idx++, t.getMes());
    }
}
