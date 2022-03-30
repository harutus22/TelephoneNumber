package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.ArrayMap
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.model.UserModel
import com.example.myapplication.utils.*
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class StartActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (getBoolean(this, IS_SUBSCRIBED)){
            if (getBoolean(this, HAVE_NUMBER)){
                startActivity(Intent(this, SmsActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, GettingNumberActivity::class.java))
                finish()
            }
        } else {
            val id = UUID.randomUUID().toString()
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager;
            val androidVersion = Build.VERSION.RELEASE
            val deviceModel = Build.MODEL
            val subStatus = 0
            val versionCode = BuildConfig.VERSION_CODE
            val simCountry = telephonyManager.networkCountryIso
            setUserId(id, this)
//            val retro = RetrofitClient.getRetrofitClient().create(ApiCall::class.java)
            val user = UserModel(userId = id, subStatus = subStatus, androidVersion = androidVersion,
                deviceModel = deviceModel, simCountry = simCountry, version = versionCode)
//            val sendUser = retro.postUser(user)
//            sendUser.enqueue(object : Callback<String> {
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    if (response.isSuccessful)
//                     Log.d("RESPONSE", response.message())
//                }
//
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    Log.d("RESPONSE", t.message.toString())
//                }
//            })
//            showCustomDialog(user)
            next.setOnClickListener {
                newUser(user)
            }
        }
    }

    private fun newUser(user: UserModel) {
        val mHeaders: MutableMap<String, String> = ArrayMap()
        mHeaders["Content-Type"] = "application/json"
        val postUrl = "https://s.5serverconnect.com/api/virtapp/newuser/"
        val requestQueue = Volley.newRequestQueue(this)
        val postData = JSONObject()
        try {
            postData.put("user_id", getUserId(this))
            postData.put("sub_status", 0)
            postData.put("android_version", user.androidVersion)
            postData.put("device_model", user.deviceModel)
            postData.put("sim_country", user.simCountry)
            postData.put("token", user.token)
            postData.put("version", user.version)
            postData.put("key", user.key)


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->
                Log.e("TAG", response.toString())
                    startActivity(Intent(this, GettingNumberActivity::class.java).putExtra(SEND_USER, user))
                    finish()
            }
        ) { error ->
            Log.e("TAG", error.message.toString())
            error?.printStackTrace() }
        requestQueue.add(jsonObjectRequest)
    }


}