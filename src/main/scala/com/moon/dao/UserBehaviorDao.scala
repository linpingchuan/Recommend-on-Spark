package com.moon.dao

import com.moon.entity._
import org.mybatis.scala.mapping.Binding._
import org.mybatis.scala.mapping._

import scala.language.postfixOps

/**
  * Created by lin on 3/8/16.
  */
object UserBehaviorDao {
  val UserBehaviorResultMap=new ResultMap[UserBehavior] {
    id(column = "user_behavior_pk_id", property = "id")
    result(column = "browse_shop_count", property = "browseShopCount")
    result(column="user_shop_menu_score",property="userShopMenuScore")

    association[Customer](property = "customer",
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
          })
      })
    association[ShopMenu](property="shopMenu",
      resultMap=new ResultMap[ShopMenu]{
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
    )

  }

  val SELECT_SQL=
    <xsql>
      SELECT *
      FROM user_behavior
    </xsql>

  val findCustomerPage=new SelectListPage[UserBehavior]{
    resultMap=UserBehaviorResultMap
    def xsql=
    <xsql>
      {SELECT_SQL}
      order by user_behavior_fk_customer_id
    </xsql>
  }

  val findShopMenuPage=new SelectListPage[UserBehavior]{
    resultMap=UserBehaviorResultMap
    def xsql=
      <xsql>
        {SELECT_SQL}
        order by user_behavior_fk_shop_menu_id
      </xsql>
  }

  val insert=new Insert[UserBehavior]{
    def xsql=
    <xsql>
      INSERT INTO user_behavior(user_behavior_fk_customer_id,user_behavior_fk_shop_menu_id,browse_shop_count,user_shop_menu_score)
      VALUES(
        #{{customer.id}},
        #{{shopMenu.id}},
        #{{browseShopCount}},
        #{{userShopMenuScore}}
      )
    </xsql>
  }


  val findById=new SelectOneBy[Long,UserBehavior]{
    resultMap=UserBehaviorResultMap
    def xsql=
    <xsql>
      {SELECT_SQL}
      WHERE user_behavior_pk_id={"id"?}
    </xsql>
  }

  def bind=Seq(findCustomerPage,findShopMenuPage,insert,findById)
}
