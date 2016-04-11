package com.moon.dao

import com.moon.entity._
import org.mybatis.scala.mapping.{SelectOneBy, SelectListPage, Insert, ResultMap}

/**
  * Created by lin on 3/13/16.
  */
object ToRecommendDao {
  val SelectCustomer = new SelectOneBy[Int, Customer]() {
    resultMap = new ResultMap[Customer] {
      id(column = "customer_pk_id", property = "id")
      result(column = "customer_pass", property = "pass")
      result(column = "customer_establish_time", property = "establishTime")
      result(column = "customer_avatar", property = "avatar")
      result(column = "customer_sex", property = "sex")
      result(column = "customer_nickname", property = "nickname")
      result(column = "customer_live_address", property = "liveAddress")
      result(column = "customer_logintime", property = "loginTime")
      result(column = "customer_loginip", property = "ip")
      result(column = "customer_signnature", property = "signnature")
      result(column = "customer_email", property = "email")
      result(column = "customer_phone", property = "phone")

      association[Money](property = "money",
        resultMap = new ResultMap[Money] {
          result(column = "money_pk_id", property = "id")
          result(column = "money_sum", property = "sum")
        }
      )
    }

    def xsql =
      <xsql>
        SELECT *
        FROM customer
        WHERE customer_pk_id=#{{id}}
      </xsql>
  }

  val SelectShopMenu = new SelectOneBy[Int, ShopMenu]() {
    resultMap = new ResultMap[ShopMenu] {
      result(column = "shop_menu_pk_id", property = "id")
      result(column = "shop_menu_name", property = "name")
      result(column = "shop_menu_intro", property = "intro")
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
        }
      )
    }

    def xsql =
      <xsql>
        SELECT *
        FROM shop_menu
        WHERE shop_menu_pk_id=#{{id}}
      </xsql>
  }
  val ToRecommendResultMap = new ResultMap[ToRecommend] {
    id(column = "to_recommend_pk_id", property = "id")
    result(column = "to_recommend_create_at", property = "createAt")

    association[Customer](property = "customer", column = "to_recommend_customer_id", select = SelectCustomer)

    association[ShopMenu](property = "shopMenu", column = "to_recommend_shop_menu_id", select = SelectShopMenu)
  }

  val SELECT_SQL =
    <xsql>
      SELECT *
      FROM to_recommend
    </xsql>

  val insert = new Insert[ToRecommend] {
    def xsql =
      <xsql>
        INSERT INTO to_recommend(
        to_recommend_customer_id,to_recommend_shop_menu_id,to_recommend_create_at)
        VALUES(
        #{{customer.id}},
        #{{shopMenu.id}},
        #{{createAt}}
        )
      </xsql>
  }

  val findCustomerPage = new SelectListPage[ToRecommend] {
    resultMap = ToRecommendResultMap

    def xsql =
      <xsql>
        {SELECT_SQL}
        order by to_recommend_create_at desc
      </xsql>
  }

  val findShopenMenuPage = new SelectListPage[ToRecommend] {
    resultMap = ToRecommendResultMap

    def xsql =
      <xsql>
        {SELECT_SQL}
        order by to_recommend_create_at desc
      </xsql>
  }

  def bind = Seq(findCustomerPage, findShopenMenuPage, insert)
}
