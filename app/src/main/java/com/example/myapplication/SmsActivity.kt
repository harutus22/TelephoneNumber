package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.blongho.country_data.World
import com.example.myapplication.model.NumberReturnModel
import com.example.myapplication.model.NumberUserModel
import com.example.myapplication.retrofit.ApiCall
import com.example.myapplication.retrofit.RetrofitClient
import com.example.myapplication.view_model.NumberViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.myapplication.adapters.SmsAdapter
import com.example.myapplication.model.SmsModel
import com.example.myapplication.utils.*
import kotlinx.android.synthetic.main.activity_number.*
import org.json.JSONException
import org.json.JSONObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_number.time
import kotlinx.android.synthetic.main.sms_item.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class SmsActivity : AppCompatActivity() {

    private lateinit var viewModel: NumberViewModel
    private lateinit var retro: ApiCall
    private lateinit var adapter: SmsAdapter
    private lateinit var number1: NumberReturnModel

    private fun getObject(): NumberReturnModel {
        val gson = Gson()
        val a = getSharedString(this, OBJECT_SAVED)
        return gson.fromJson(a, NumberReturnModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number)

       //intent.getParcelableExtra<NumberReturnModel>(NUMBER)

        retro = RetrofitClient.getRetrofitClient().create(ApiCall::class.java)
        viewModel = ViewModelProvider(this).get(NumberViewModel::class.java)
        adapter = SmsAdapter()

        rec.adapter = adapter

//        viewModel.getNumber().observe(this, {pic ->
//            if (number1 == null)
//                number1 = pic
//            numberShow.text = pic?.number
//        })

        viewModel.getSms().observe(this, {
            adapter.insertList(it)
        })

        get_sms.setOnClickListener {
            postSms()
        }

        back_arrow.setOnClickListener {
            startActivity(Intent(this, GettingNumberActivity::class.java))
        }

        new_number.setOnClickListener {
            startActivity(Intent(this, GettingNumberActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        number1 = getObject()
        numberShow.text = number1.number
        World.init(getApplicationContext())
        time.text = getTimerText(number1.time)
        flag.setImageResource(World.getFlagOf(number1.country))
    }

    private fun convertTime(time: Int): String{
        val calendar: Calendar = Calendar.getInstance()
//        val tz: TimeZone = TimeZone.getDefault()
//        calendar.add(Calendar.MILLISECOND, tz.getOffset(time.toLong()))
//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//        val currenTimeZone = Date(time.toLong() * 1000)
//        return sdf.format(currenTimeZone)

        val formatter: DateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss z")
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"))
        val a = calendar.time
        return formatter.format(a)
    }

    private fun getTimerText(time: Int): String {
        val date = SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Date(time.toLong()*1000))
//        val seconds = ((time % 86400) % 3600) % 60
//        val minutes = ((time % 86400) % 3600) / 60
//        val hours = ((time % 86400) / 3600)
//        val days = time % 86400
        return date
    }

    private fun formatTime(seconds: Int, minutes: Int, hours: Int, days: Int): String {
        return String.format("%02d", days) + " days " + String.format("%02d", hours) + ":" +
                String.format("%02d", minutes) + ":" + String.format("%02d", seconds)
    }

    private fun postSms() {
        val sms = retro.getSms(NumberUserModel(id = number1.userId))
        sms.enqueue(object : Callback<List<SmsModel>>{
            override fun onResponse(
                call: Call<List<SmsModel>>,
                response: Response<List<SmsModel>>
            ) {
                val list = response.body()
                adapter.insertList(list!!)
                viewModel.deleteSms()
                viewModel.insertSms(list)
            }

            override fun onFailure(call: Call<List<SmsModel>>, t: Throwable) {
                Log.d("RESPONSE", t.localizedMessage)
            }

        })
//        volleyPost(number)
    }

    private fun volleyPost(number: String) {
        val mHeaders: MutableMap<String, String> = ArrayMap()
        mHeaders["Content-Type"] = "application/json"
        val postUrl = "https://s.5serverconnect.com/api/virtapp/getsms/"
        val requestQueue = Volley.newRequestQueue(this)
        val postData = JSONObject()
        try {
            val id =  getUserId(this)
            postData.put("number_user_id", number)
            postData.put("key", "")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            postUrl,
            postData,
            { response ->
                val id = response["number_id"] as Int
                val userId = response["number_user_id"] as String
            }
        ) { error ->
            error?.printStackTrace() }
        requestQueue.add(jsonObjectRequest)
    }
}