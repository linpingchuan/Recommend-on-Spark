package com.moon.dao

import com.moon.entity.{Money, Shop}
import org.mybatis.scala.mapping.{Insert, SelectListPage, ResultMap}

/**
  * Created by lin on 3/8/16.
  */
object ShopDao {
  val ShopResultMap=new ResultMap[Shop]{
    id(column="shop_pk_id",property="id")
    result(column="shop_name",property="name")
    result(column="shop_address",property="address")
    result(column="shop_tel",property="tel")
    result(column="shop_boss",property="boss")
    result(column="shop_intro",property="intro")
    result(column="shop_establish_time",property="establishTime")
    result(column="shop_password",property="pass")
    result(column="shop_status",property="status")

    association[Money](property="money",
      resultMap=new ResultMap[Money]{
        result(column="money_pk_id",property="id")
        result(column="money_sum",property="sum")
      })
  }

  val SELECT_SQL=
    <xsql>
      SELECT *
      FROM shop
    </xsql>

  val findShopPage=new SelectListPage[Shop]{
    resultMap=ShopResultMap
    def xsql=
      <xsql>
        SELECT_SQL
        ORDER BY shop_establish_time desc
      </xsql>
  }

  val insert=new Insert[Shop]{
    def xsql=
    <xsql>
      INSERT INTO shop(shop_pk_id,shop_name,shop_address,shop_tel,shop_boss,shop_intro,shop_establish_time,shop_password,shop_status,shop_fk_money_id)
      VALUES(
        #{{id}},
        #{{name}},
        #{{address}},
        #{{tel}},
        #{{boss}},
        #{{intro}},
        #{{establishTime}},
        #{{pass}},
        #{{status}},
        #{{money.id}}
      )
    </xsql>
  }

  def bind=Seq(findShopPage,insert)
}
