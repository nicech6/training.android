package com.life.login


import android.Manifest
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.NonNull
import com.alibaba.android.arouter.facade.annotation.Route
import com.life.base.constant.Path
import com.life.base.entity.LoginEntity
import com.life.base.mvp.BaseMvpActivity
import com.life.base.permission.MPermission
import com.life.base.permission.annotation.OnMPermissionDenied
import com.life.base.permission.annotation.OnMPermissionGranted
import com.life.base.permission.annotation.OnMPermissionNeverAskAgain
import com.life.base.util.ToastUtils
import kotlinx.android.synthetic.main.activity_login.*

@Route(path = Path.LOGIN_MAI)
class LoginMvpActivity : BaseMvpActivity<LoginPresenter>(), LoginContact.View {

    companion object {
        const val BASIC_PERMISSION_REQUEST_CODE = 0x100
    }

    override fun bindEvent() {
        btn.setOnClickListener {
            mPresenter.getLogin(et_username.text.toString(), et_pwd.text.toString())
        }
    }

    override fun onLogin(entity: LoginEntity?) {

    }

    private fun requestPermiss() {
        val permiss = arrayOf(Manifest.permission.READ_PHONE_STATE)
        val permissGrant = MPermission.getDeniedPermissions(this, permiss)
        if (null == permissGrant || permissGrant!!.size > 0) {
            MPermission.with(this)
                    .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                    .permissions(*permiss)
                    .request()
        }
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    fun onBasicPermissionSuccess() {
        onPermissionChecked()
    }

    private fun onPermissionChecked() {
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    fun onBasicPermissionFailed() {
        ToastUtils.showLong("hehe", Toast.LENGTH_SHORT)
        onPermissionChecked()
    }

    override fun initView() {

    }

    override fun initData() {
        requestPermiss()
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
