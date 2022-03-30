package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.adapters.CountryNumberAdapter
import com.example.myapplication.model.CountryModel
import com.example.myapplication.model.NumberReturnModel
import com.example.myapplication.model.UserModel
import com.example.myapplication.repo.MoneyType
import com.example.myapplication.repo.localdb.LakeDetails
import com.example.myapplication.retrofit.ApiCall
import com.example.myapplication.retrofit.RetrofitClient
import com.example.myapplication.utils.*
import com.example.myapplication.view_model.NumberViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GettingNumberActivity : AppCompatActivity(), OnCountryChoseClick {

    private lateinit var moneyType: MoneyType
    private lateinit var lakeDetails: List<LakeDetails>
    private lateinit var user: UserModel

    private lateinit var retro: ApiCall
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        retro = RetrofitClient.getRetrofitClient().create(ApiCall::class.java)

        if (intent.hasExtra(SEND_USER)) {
            user = intent.getParcelableExtra<UserModel>(SEND_USER)!!
            setShared(user)
        }
        viewModel = ViewModelProvider(this).get(NumberViewModel::class.java)

//        search.isFocusable = true;
//        search.isIconified = false;
        search.onActionViewExpanded();
        search.clearFocus()

        val numbers = retro.getNumbersCount()
        progress.visibility = View.VISIBLE
        numbers.enqueue(object : Callback<List<CountryModel>> {
            override fun onResponse(
                call: Call<List<CountryModel>>,
                response: Response<List<CountryModel>>
            ) {
                initRecycler(ArrayList(response.body()))
                progress.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<CountryModel>>, t: Throwable) {
                Log.d("RESPONSE", t.message.toString())
            }

        })

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (recycler_view.adapter as CountryNumberAdapter).filter.filter(newText)
                return false
            }

        })

        moneyType = ViewModelProviders.of(this)[MoneyType::class.java]

        moneyType.subsSkuDetailsListLiveData.observe(this,
            { a -> //setSkuDetailsList(it)
                lakeDetails = a
                Log.d("testt", a.toString())
            })
    }

    private fun initRecycler(list: ArrayList<CountryModel>?) {
        recycler_view.adapter = CountryNumberAdapter(this, list!!, this)
    }

    private fun volleyPost(country: String) {
        val mHeaders: MutableMap<String, String> = ArrayMap()
        mHeaders["Content-Type"] = "application/json"
        val postUrl = "https://s.5serverconnect.com/api/virtapp/getnumber/"
        val requestQueue = Volley.newRequestQueue(this)
        val postData = JSONObject()
        try {
            val id = getUserId(this)
            postData.put("user_id", id)
            postData.put("country", country)
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
                val number = response["phone_number"] as String
                val lastActive = response["last_active"] as Int
                setBoolean(HAVE_NUMBER, true, this)
                val numberReturnModel = NumberReturnModel(
                    id = id,
                    userId = userId,
                    number = number,
                    time = lastActive,
                    country = country
                )
                setShared(numberReturnModel)
//                viewModel.insert(numberReturnModel)
                startActivity(
                    Intent(this, SmsActivity::class.java).putExtra(
                        NUMBER,
                        numberReturnModel
                    )
                )
                finish()
            }
        ) { error ->
            error?.printStackTrace()
        }
        requestQueue.add(jsonObjectRequest)
    }

    private fun setShared(model: NumberReturnModel) {
        val gson = Gson();
        val json = gson.toJson(model);
        setString(OBJECT_SAVED, json, this)
    }

    private fun setShared(model: UserModel) {
        val gson = Gson();
        val json = gson.toJson(model);
        setString(USER_SAVED, json, this)
    }

    private lateinit var viewModel: NumberViewModel

    override fun onFlagClick(countryModel: CountryModel) {
        if (getBoolean(this, HAVE_NUMBER)) {
            viewModel.deleteSms()
            viewModel.delete()
        }
        if (getBoolean(this, IS_SUBSCRIBED))
            volleyPost(countryModel.country)
        else
            showCustomDialog()
    }

    private fun newUser(user: UserModel) {
        val mHeaders: MutableMap<String, String> = ArrayMap()
        mHeaders["Content-Type"] = "application/json"
        val postUrl = "https://s.5serverconnect.com/api/virtapp/newuser/"
        val requestQueue = Volley.newRequestQueue(this)
        val postData = JSONObject()
        try {
            postData.put("user_id", getUserId(this))
            postData.put("sub_status", 1)
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
                setBoolean(IS_SUBSCRIBED, true, context = application)
            }
        ) { error ->
            Log.e("TAG", error.message.toString())
            error?.printStackTrace()
        }
        requestQueue.add(jsonObjectRequest)
    }

    private fun showCustomDialog() {
        if (!this::lakeDetails.isInitialized || lakeDetails.isEmpty()) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT)
                .show()
        } else {
            onPurchase(lakeDetails[0])
        }

    }

    private fun onPurchase(item: LakeDetails) {
        moneyType.makePurchase(this, item)
        Log.d("testt", "starting purchase flow for SkuDetail:\n \$item")
    }
}