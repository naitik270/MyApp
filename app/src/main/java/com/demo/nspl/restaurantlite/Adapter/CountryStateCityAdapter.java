package com.demo.nspl.restaurantlite.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.nspl.restaurantlite.Country.New.City.ClsCity;
import com.demo.nspl.restaurantlite.Country.New.Country.ClsCountry;
import com.demo.nspl.restaurantlite.Country.New.State.ClsState;
import com.demo.nspl.restaurantlite.R;
import com.demo.nspl.restaurantlite.activity.CountryStateCityListActivity;

import java.util.ArrayList;
import java.util.List;

import static com.demo.nspl.restaurantlite.Global.ClsGlobal.countryStateCityCode;

public class CountryStateCityAdapter extends RecyclerView.Adapter<CountryStateCityAdapter.CountryHolder> implements Filterable {

    private ArrayList<ClsCountry> countries;
    private ArrayList<ClsCountry> countriesCopy;

    private ArrayList<ClsState> states;
    private ArrayList<ClsState> statesCopy;

    private ArrayList<ClsCity> cities;
    private ArrayList<ClsCity> citiesCopy;
    Context context;

    public interface onCountryClickListener {
        void onCountryClick(ClsCountry clsCountry);
    }

    public interface onStateClickListener {
        void onStateClick(ClsState clsState);
    }

    public interface onCityClickListener {
        void onCityClick(ClsCity clsCity);
    }

    onCountryClickListener countryListener;
    onStateClickListener stateListener;
    onCityClickListener cityListener;


    public CountryStateCityAdapter(ArrayList<ClsCountry> countries, Context context, onCountryClickListener countryListener) {
        this.countries = countries;
        this.context = context;
        this.countryListener = countryListener;
        this.countriesCopy = new ArrayList<>(countries);
    }

    public CountryStateCityAdapter(ArrayList<ClsState> states, onStateClickListener stateListener) {
        this.states = states;
        this.stateListener = stateListener;
        this.statesCopy = new ArrayList<>(states);
    }

    public CountryStateCityAdapter(ArrayList<ClsCity> cities, onCityClickListener cityListener) {
        this.cities = cities;
        this.cityListener = cityListener;
        this.citiesCopy = new ArrayList<>(cities);
    }

    @NonNull
    @Override
    public CountryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_country_state_city, parent, false);
        return new CountryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryHolder holder, int position) {
        if (countryStateCityCode == 1) {
            ClsCountry clsCountry = countries.get(position);
            holder.tvCountryCode.setText(clsCountry.getCountryID().toString() + "");
            holder.tvCountry.setText(clsCountry.getCountryName().toUpperCase());
            holder.llCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    countryListener.onCountryClick(countries.get(position));

                }
            });
        } else if (countryStateCityCode == 2) {
            ClsState clsState = states.get(position);
            holder.tvCountryCode.setText(clsState.getStateID().toString() + "");
            holder.tvCountry.setText(clsState.getStateName().toUpperCase());
            holder.llCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    stateListener.onStateClick(states.get(position));
                }
            });

        } else {
            ClsCity clsCity = cities.get(position);
            holder.tvCountryCode.setText(clsCity.getCityID().toString() + "");
            holder.tvCountry.setText(clsCity.getCityName().toUpperCase());
            holder.llCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cityListener.onCityClick(cities.get(position));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (countryStateCityCode == 1) {
            return countries.size();
        } else if (countryStateCityCode == 2) {
            return states.size();
        } else {
            return cities.size();
        }
    }

    public class CountryHolder extends RecyclerView.ViewHolder {
        TextView tvCountry, tvCountryCode;
        LinearLayout llCountry;

        public CountryHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvCountryCode = itemView.findViewById(R.id.tvCountryCode);
            llCountry = itemView.findViewById(R.id.llCountry);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            if (countryStateCityCode == 1) {
                List<ClsCountry> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(countriesCopy);


                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (ClsCountry country : countriesCopy) {
                        if (country.getCountryName().toLowerCase().trim().contains(filterPattern)) {
                            filteredList.add(country);
                        }

                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            } else if (countryStateCityCode == 2) {
                List<ClsState> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(statesCopy);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (ClsState state : statesCopy) {
                        if (state.getStateName().toLowerCase().trim().contains(filterPattern)) {
                            filteredList.add(state);
                        }

                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            } else {
                List<ClsCity> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(citiesCopy);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for (ClsCity city : citiesCopy) {
                        if (city.getCityName().toLowerCase().trim().contains(filterPattern)) {
                            filteredList.add(city);
                        }

                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {


            if (countryStateCityCode == 1) {
                countries.clear();
                countries.addAll((List) filterResults.values);
                notifyDataSetChanged();
                CountryStateCityListActivity.checkArrayListCountry(countries);

            } else if (countryStateCityCode == 2) {
                states.clear();
                states.addAll((List) filterResults.values);
                notifyDataSetChanged();
                CountryStateCityListActivity.checkArrayListState(states);

            } else {
                cities.clear();
                cities.addAll((List) filterResults.values);
                notifyDataSetChanged();
                CountryStateCityListActivity.checkArrayListCity(cities);

            }
        }
    };

}
