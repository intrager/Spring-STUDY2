package org.zerohan.exzero.mappers;

import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

public interface TimeMapper {
    /*
    스프링이 실제 객체를 cglib을 이용해서 생성하여 씀. 
     */
    @Select("select sysdate from dual")
    LocalDateTime getTime();

    LocalDateTime getTimeUsingMapper();
}
