package com.moon.dao

import com.moon.entity.Money
import org.mybatis.scala.mapping.{Insert, SelectListPage, ResultMap}

/**
  * Created by lin on 3/9/16.
  */
object MoneyDao {
  val MoenyResultMap=new ResultMap[Money]{
    id(column="money_pk_id",property="id")
    result(column="money_sum",property="sum")
  }

  val SELECT_SQL=
    <xsql>
      SELECT *
      FROM money
    </xsql>

  val findMoneyPage=new SelectListPage[Money]{
    def xsql=
    <xsql>
      SELECT_SQL
      order by money_pk_id
    </xsql>
  }

  val insert=new Insert[Money]{
    def xsql=
    <xsql>
      INSERT INTO money(money_sum)
      VALUES(
      #{{sum}}
      )
    </xsql>
  }

  def bind=Seq(findMoneyPage,insert)
}
