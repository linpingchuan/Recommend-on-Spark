package com.moon.dao

import com.moon.entity.{Money, Shop, ShopMenu}
import org.mybatis.scala.mapping.{Insert, ResultMap}

/**
  * Created by lin on 3/30/16.
  */
object ShopMenuDao {
  val ShopMenuResultMap = new ResultMap[ShopMenu]{
    id(column="shop_menu_pk_id",property="id")
    result(column="shop_menu_name",property="name")
    result(column="shop_menu_intro",property="intro")

    association[Shop](property = "shop",
      resultMap = new ResultMap[Shop] {
        id(column = "shop_pk_id", property = "id")
        result(column = "shop_name", property = "name")
        result(column = "shop_address", property = "address")
        result(column = "shop_tel", property = "tel")
        result(column = "shop_boss", property = "boss")
        result(column = "shop_intro", property = "intro")
        result(column = "shop_establish_time", property = "establishTime")
        result(column = "shop_password", property = "pass")
        result(column = "shop_status", property = "status")
        association[Money](property = "money",
          resultMap = new ResultMap[Money] {
            result(column = "money_pk_id", property = "id")
            result(column = "money_sum", property = "sum")
          })
      })
  }

  val insert=new Insert[ShopMenu]{
    def xsql=
    <xsql>
      INSERT INTO shop_menu(
        shop_menu_pk_id,shop_menu_fk_shop_id,shop_menu_name,shop_menu_intro)
      VALUES(
        #{{id}},
        #{{shop.id}},
        #{{name}},
        #{{intro}}
      )
    </xsql>
  }

  def bind=Seq(insert)
}
