package com.ch.login


import com.alibaba.android.arouter.facade.annotation.Route
import com.ch.base.constant.Path
import com.ch.base.entity.LoginEntity
import com.ch.base.mvp.BaseMvpActivity
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

    override fun needImmersion(): Boolean {
        return true
    }
}
