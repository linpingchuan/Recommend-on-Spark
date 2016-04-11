package com.moon.dao

import com.moon.entity.{Money, Customer}
import org.mybatis.scala.mapping.{Insert, SelectListPage, ResultMap}

/**
  * Created by lin on 3/9/16.
  */
object CustomerDao {
  val CustomerResultMap=new ResultMap[Customer]{
    id(column="customer_pk_id",property="id")
    result(column="customer_pass",property="pass")
    result(column="customer_establish_time",property="establishTime")
    result(column="customer_avatar",property="avatar")
    result(column="customer_sex",property="sex")
    result(column="customer_nickname",property="nickname")
    result(column="customer_live_address",property="liveAddress")
    result(column="customer_logintime",property="loginTime")
    result(column="customer_loginip",property="ip")
    result(column="customer_signnature",property="signnature")
    result(column="customer_email",property="email")
    result(column="customer_phone",property="phone")

    association[Money](property="money",
      resultMap=new ResultMap[Money]{
        result(column="money_pk_id",property="id")
        result(column="money_sum",property="sum")
      }
    )
  }

  val SELECT_SQL=
    <xsql>
      SELECT *
      FROM customer
    </xsql>

  val findCustomerPage=new SelectListPage[Customer]{
    def xsql=
    <xsql>
      SELECT_SQL
      ORDER BY customer_establish_time desc
    </xsql>
  }

  val insert=new Insert[Customer]{
    def xsql=
    <xsql>
      INSERT INTO customer(customer_pk_id,customer_pass,customer_establish_time,customer_avatar,customer_sex,customer_nickname,customer_live_address,customer_logintime,customer_loginip,customer_signnature,customer_email,customer_phone,customer_fk_money_id)
      VALUES(
      #{{id}},
      #{{pass}},
      #{{establishTime}},
      #{{avatar}},
      #{{sex}},
      #{{nickname}},
      #{{liveAddress}},
      #{{loginTime}},
      #{{ip}},
      #{{signnature}},
      #{{email}},
      #{{phone}},
      #{{money.id}}
      )
    </xsql>
  }

  def bind=Seq(findCustomerPage,insert)
}
