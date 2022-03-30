package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blongho.country_data.World
import com.example.myapplication.R
import com.example.myapplication.model.CountryModel
import com.example.myapplication.utils.OnCountryChoseClick
import me.ibrahimsn.lib.PhoneNumberKit


class CountryNumberAdapter(
    private val onCountryChoseClick: OnCountryChoseClick,
    private val countryList: ArrayList<CountryModel>,
    context: Context): RecyclerView.Adapter<CountryNumberAdapter.CountryViewHolder>(), Filterable {
    private var util: PhoneNumberKit = PhoneNumberKit(context)
    private val countryListFilter: ArrayList<CountryModel> = ArrayList(countryList)
    private val filter = object : Filter(){
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = ArrayList<CountryModel>()
            if (p0.toString().isEmpty())
                filteredList.addAll(countryListFilter)
            else {
                for (country in countryListFilter){
                    if (World.getCountryFrom(country.country).name.toLowerCase().startsWith(p0.toString().toLowerCase()))
                        filteredList.add(country)
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList

            return filterResults
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            countryList.clear()
            countryList.addAll(p1?.values as Collection<CountryModel>)
            notifyDataSetChanged();
        }

    }
    init {
        World.init(context)
    }


        inner class CountryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            private val image: ImageView = itemView.findViewById(R.id.image)
            private val number: TextView = itemView.findViewById(R.id.availableNumbers)
            private val countryName: TextView = itemView.findViewById(R.id.countryName)
            private val countryCode: TextView = itemView.findViewById(R.id.countryCode)

            fun bind(countryModel: CountryModel){
                val mod = World.getCountryFrom(countryModel.country)
                image.setImageResource(World.getFlagOf(countryModel.country))
                countryName.text = mod.name

                countryCode.text = "+${util.getCountry(countryModel.country)?.countryCode.toString()}"
                number.text = countryModel.emptyNumbers.toString()
                itemView.setOnClickListener {
                    onCountryChoseClick.onFlagClick(countryModel)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coutry_item, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countryList[position])
    }

    override fun getItemCount() = countryList.size
    override fun getFilter(): Filter {
        return filter
    }
}