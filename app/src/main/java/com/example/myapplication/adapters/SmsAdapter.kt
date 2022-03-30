package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.SmsModel
import java.util.concurrent.TimeUnit

class SmsAdapter(): RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    private var smsList: ArrayList<SmsModel> = ArrayList()

        inner class SmsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val title: TextView = itemView.findViewById(R.id.title)
            private val body: TextView = itemView.findViewById(R.id.body)
//            private val time: TextView = itemView.findViewById(R.id.time)
            private val date: TextView = itemView.findViewById(R.id.date)

            fun bind(smsModel: SmsModel){
                title.text = smsModel.sms_adress
                body.text = smsModel.sms_body
//                time.text = String.format("%02d:%02d:%02d",
//                    TimeUnit.MILLISECONDS.toHours(smsModel.sms_senttime),
//                    TimeUnit.MILLISECONDS.toMinutes(smsModel.sms_senttime) -
//                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(smsModel.sms_senttime)),
//                    TimeUnit.MILLISECONDS.toSeconds(smsModel.sms_senttime) -
//                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(smsModel.sms_senttime)))
                date.text = smsModel.date
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sms_item, parent, false)
        return SmsViewHolder(view)
    }

    fun insertList(smsList: List<SmsModel>){
        this.smsList.clear()
        this.smsList = ArrayList(smsList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        holder.bind(smsList[position])
    }

    override fun getItemCount() = smsList.size
}