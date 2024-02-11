/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.demo.mapper;

import com.example.demo.model.Bpi;
import com.example.demo.util.MyMapper;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BpiMapper extends MyMapper<Bpi> {

    @Insert("insert into BPI (NAME, CODE, SYMBOL, RATE, DESCRIPTION, RATE_FLOAT) VALUES (#{name}, #{code}, #{symbol}, #{rate}, #{description}, #{rateFloat})")
    public int create(Bpi emp);

    @Select("select * from Bpi where code = #{code}")
    Bpi findByCode(@Param("code") String code);


    @Delete("delete from BPI where ID=#{id} ")
    public boolean deleteOne(@Param("id") Integer id);
}