package com.life.login


import com.alibaba.android.arouter.facade.annotation.Route
import com.life.base.constant.Path
import com.life.base.entity.LoginEntity
import com.life.base.mvp.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_login.*

@Route(path = Path.LOGIN_MAI)
class LoginMvpActivity : BaseMvpActivity<LoginPresenter>(), LoginContact.View {
    override fun bindEvent() {
        btn.setOnClickListener {
            mPresenter.getLogin(et_username.text.toString(), et_pwd.text.toString())
        }
    }

    override fun onLogin(entity: LoginEntity?) {

    }

    override fun initView() {}

    override fun initData() {

    }

    override fun initLayout(): Int {
        return R.layout.activity_login
    }

    override fun initPresenter(): LoginPresenter {
        return LoginPresenter(this, this)
    }

    override fun immersion(): Boolean {
        return true
    }
}
