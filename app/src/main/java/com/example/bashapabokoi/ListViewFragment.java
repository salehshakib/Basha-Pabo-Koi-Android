package com.example.bashapabokoi;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.bashapabokoi.Adapters.AdsAdapter;
import com.example.bashapabokoi.Helper.LocaleHelper;
import com.example.bashapabokoi.Models.CreateAd;
import com.example.bashapabokoi.databinding.FragmentListadsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class ListViewFragment extends Fragment {

    FragmentListadsBinding binding;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ArrayList<CreateAd> allAds;
    AdsAdapter adsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){

            Objects.requireNonNull(getContext()).setTheme(R.style.Theme_BashaPaboKoi_Dark);
        } else{

            Objects.requireNonNull(getContext()).setTheme(R.style.Theme_BashaPaboKoi);
        }

        super.onCreate(savedInstanceState);
        binding = FragmentListadsBinding.inflate(inflater, container, false);

        allAds = new ArrayList<>();
        adsAdapter = new AdsAdapter(getContext(), allAds);
        binding.recyclerView.setAdapter(adsAdapter);


        database.getReference().child("All_ad").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){

                    String[] arrOfStr = Objects.requireNonNull(ds.getKey()).split("-");
                    for (String s : arrOfStr){
                        if(!Objects.equals(auth.getUid(), s)){

                            CreateAd showAds = ds.getValue(CreateAd.class);

                            allAds.add(showAds);

                            break;
                        }
                        break;
                    }



                }
                Collections.reverse(allAds);
                adsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Paper.init(Objects.requireNonNull(getContext()));

        String language = Paper.book().read("language");
        if(language == null){

            Paper.book().write("language", "en");
        }

        Context context = LocaleHelper.setLocale(getContext(), language);
        Resources resources = context.getResources();

        List<String> thanaCategories = new ArrayList<>();
        List<String> thanaCategoriesBn = new ArrayList<>();
        List<String> thanaCategoriesEn = new ArrayList<>();

        thanaCategories.add(resources.getString(R.string.search_by_location));
        thanaCategories.add(resources.getString(R.string.adabar));
        thanaCategories.add(resources.getString(R.string.zimpur));
        thanaCategories.add(resources.getString(R.string.badda));
        thanaCategories.add(resources.getString(R.string.bangshal));
        thanaCategories.add(resources.getString(R.string.bimanbandar));
        thanaCategories.add(resources.getString(R.string.cantonment));
        thanaCategories.add(resources.getString(R.string.chowkbazar));
        thanaCategories.add(resources.getString(R.string.darus_salam));
        thanaCategories.add(resources.getString(R.string.demra));
        thanaCategories.add(resources.getString(R.string.dhanmondi));
        thanaCategories.add(resources.getString(R.string.gendaria));
        thanaCategories.add(resources.getString(R.string.gulshan));
        thanaCategories.add(resources.getString(R.string.hazaribagh));
        thanaCategories.add(resources.getString(R.string.kadamtali));
        thanaCategories.add(resources.getString(R.string.kafrul));
        thanaCategories.add(resources.getString(R.string.kalabagan));
        thanaCategories.add(resources.getString(R.string.kamrangirchar));
        thanaCategories.add(resources.getString(R.string.khilgaon));
        thanaCategories.add(resources.getString(R.string.khilkhet));
        thanaCategories.add(resources.getString(R.string.kotwali));
        thanaCategories.add(resources.getString(R.string.lalbagh));
        thanaCategories.add(resources.getString(R.string.mirpur_model));
        thanaCategories.add(resources.getString(R.string.mohammadpur));
        thanaCategories.add(resources.getString(R.string.motijheel));
        thanaCategories.add(resources.getString(R.string.new_market));
        thanaCategories.add(resources.getString(R.string.pallabi));
        thanaCategories.add(resources.getString(R.string.paltan));
        thanaCategories.add(resources.getString(R.string.panthapath));
        thanaCategories.add(resources.getString(R.string.ramna));
        thanaCategories.add(resources.getString(R.string.rampura));
        thanaCategories.add(resources.getString(R.string.sabujbagh));
        thanaCategories.add(resources.getString(R.string.shah_ali));
        thanaCategories.add(resources.getString(R.string.shahbag));
        thanaCategories.add(resources.getString(R.string.sher_e_bangla));
        thanaCategories.add(resources.getString(R.string.shyampur));
        thanaCategories.add(resources.getString(R.string.sutrapur));
        thanaCategories.add(resources.getString(R.string.tejgaon_industrial_area));
        thanaCategories.add(resources.getString(R.string.tejgaon));
        thanaCategories.add(resources.getString(R.string.turag));
        thanaCategories.add(resources.getString(R.string.uttar_khan));
        thanaCategories.add(resources.getString(R.string.uttara));
        thanaCategories.add(resources.getString(R.string.vatara));
        thanaCategories.add(resources.getString(R.string.wari));

        thanaCategoriesBn.add(resources.getString(R.string.search_by_location));
        thanaCategoriesBn.add("আদাবর");
        thanaCategoriesBn.add("আজিমপুর");
        thanaCategoriesBn.add("বাড্ডা");
        thanaCategoriesBn.add("বংশাল");
        thanaCategoriesBn.add("বিমানবন্দর");
        thanaCategoriesBn.add("ক্যান্টনমেন্ট");
        thanaCategoriesBn.add("চকবাজার");
        thanaCategoriesBn.add("দারুস-সালাম");
        thanaCategoriesBn.add("ডেমরা");
        thanaCategoriesBn.add("ধানমন্ডি");
        thanaCategoriesBn.add("গেন্ডারিয়া");
        thanaCategoriesBn.add("গুলশান");
        thanaCategoriesBn.add("হাজারিবাগ");
        thanaCategoriesBn.add("কদমতলি");
        thanaCategoriesBn.add("কাফরুল");
        thanaCategoriesBn.add("কলাবাগান");
        thanaCategoriesBn.add("কামরাঙ্গিরচর");
        thanaCategoriesBn.add("খিলগাও");
        thanaCategoriesBn.add("খিলক্ষেত");
        thanaCategoriesBn.add("কোতওয়ালি");
        thanaCategoriesBn.add("লালবাগ");
        thanaCategoriesBn.add("মিরপুর মডেল");
        thanaCategoriesBn.add("মোহাম্মদপুর");
        thanaCategoriesBn.add("মতিঝিল");
        thanaCategoriesBn.add("নিঊ মার্কেট");
        thanaCategoriesBn.add("পল্লবি");
        thanaCategoriesBn.add("পল্টন");
        thanaCategoriesBn.add("পান্থপথ");
        thanaCategoriesBn.add("রমনা");
        thanaCategoriesBn.add("রামপুরা");
        thanaCategoriesBn.add("সবুজবাগ");
        thanaCategoriesBn.add("শাহ্‌ আলী");
        thanaCategoriesBn.add("শাহ্\u200Cবাগ‌");
        thanaCategoriesBn.add("শের-এ-বাংলা");
        thanaCategoriesBn.add("শ্যামপুর");
        thanaCategoriesBn.add("সুত্রাপুর");
        thanaCategoriesBn.add("তেজগাঁও বাণিজ্যিক এরিয়া");
        thanaCategoriesBn.add("তেজগাঁও");
        thanaCategoriesBn.add("তুরাগ");
        thanaCategoriesBn.add("উত্তর খান");
        thanaCategoriesBn.add("উত্তরা");
        thanaCategoriesBn.add("ভাটারা");
        thanaCategoriesBn.add("ওয়ারী");

        thanaCategoriesEn.add(resources.getString(R.string.search_by_location));
        thanaCategoriesEn.add("Adabor");
        thanaCategoriesEn.add("Azimpur");
        thanaCategoriesEn.add("Badda");
        thanaCategoriesEn.add("Bangshal");
        thanaCategoriesEn.add("Bimanbandar");
        thanaCategoriesEn.add("Cantonment");
        thanaCategoriesEn.add("Chowkbazar");
        thanaCategoriesEn.add("Darus Salam");
        thanaCategoriesEn.add("Demra");
        thanaCategoriesEn.add("Dhanmondi");
        thanaCategoriesEn.add("Gendaria");
        thanaCategoriesEn.add("Gulshan");
        thanaCategoriesEn.add("Hazaribagh");
        thanaCategoriesEn.add("Kadamtali");
        thanaCategoriesEn.add("Kafrul");
        thanaCategoriesEn.add("Kalabagan");
        thanaCategoriesEn.add("Kamrangirchar");
        thanaCategoriesEn.add("Khilgaon");
        thanaCategoriesEn.add("Khilkhet");
        thanaCategoriesEn.add("Kotwali");
        thanaCategoriesEn.add("Lalbagh");
        thanaCategoriesEn.add("Mirpur Model");
        thanaCategoriesEn.add("Mohammadpur");
        thanaCategoriesEn.add("Motijheel");
        thanaCategoriesEn.add("New Market");
        thanaCategoriesEn.add("Pallabi");
        thanaCategoriesEn.add("Paltan");
        thanaCategoriesEn.add("Panthapath");
        thanaCategoriesEn.add("Ramna");
        thanaCategoriesEn.add("Rampura");
        thanaCategoriesEn.add("Sabujbagh");
        thanaCategoriesEn.add("Shah Ali");
        thanaCategoriesEn.add("Shahbag");
        thanaCategoriesEn.add("Sher-e-Bangla");
        thanaCategoriesEn.add("Shyampur");
        thanaCategoriesEn.add("Sutrapur");
        thanaCategoriesEn.add("Tejgaon Industrial Area");
        thanaCategoriesEn.add("Tejgaon");
        thanaCategoriesEn.add("Turag");
        thanaCategoriesEn.add("Uttar Khan");
        thanaCategoriesEn.add("Uttara");
        thanaCategoriesEn.add("Vatara");
        thanaCategoriesEn.add("Wari");

        ArrayAdapter<String> thanaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, thanaCategories);
        thanaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.thana.setAdapter(thanaAdapter);

        ArrayAdapter<String> thanaAdapterEn = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, thanaCategoriesEn);
        thanaAdapterEn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.thana.setAdapter(thanaAdapterEn);

        ArrayAdapter<String> thanaAdapterBn = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, thanaCategoriesBn);
        thanaAdapterBn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.thana.setAdapter(thanaAdapterBn);

        return binding.getRoot();
    }
}
