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
 *//**
 * Author:  murilo
 * Created: 05/11/2019
 */
drop database fipe;
create database fipe;
use fipe;
create table tabelas (tabela_id int unsigned not null auto_increment, codigo int unsigned not null,  mes char(20) not null, primary key (tabela_id));
insert into tabelas (codigo, mes) values (248,"novembro/2019");

create table marcas (marca_id int unsigned not null auto_increment, tabela_id int unsigned not null, tipo enum('CARROS','MOTOS','CAMINHOES') not null,value char(10), label varchar(1024) not null, primary key (marca_id));
create table modelos (modelo_id int unsigned not null auto_increment, marca_id int unsigned not null, tabela_id int unsigned not null, value char(10), label varchar(1024) not null, primary key (modelo_id));
create table anos_modelos (ano_modelo_id int unsigned not null auto_increment, modelo_id int unsigned not null, marca_id int unsigned not null, tabela_id int unsigned not null, value char(10), label varchar(1024) not null, primary key (ano_modelo_id));

