package com.kutub.youngorganization;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ProminentPersonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prominent_person);

        LinearLayout container = findViewById(R.id.prominentPersonContainer);

        // অনেক ব্যক্তির ডেটা লিস্ট
        class Person {
            String name, facebook, youtube;

            Person(String n, String f, String y) {
                name = n;
                facebook = f;
                youtube = y;
            }
        }
        Person[] prominentPeople = new Person[] {
                new Person("পিনাকি ভট্টাচার্য (অধিকারকর্মী)", "https://www.facebook.com/PinakiRightsActivist/", "https://www.youtube.com/@PinakiBhattacharya"),
                new Person("ইলিয়াস হোসেন (সাংবাদিক)", "https://www.facebook.com/EliasHossain/", "https://www.youtube.com/c/EliasHossain"),
                new Person("শেখ আহমাদুল্লাহ (ইসলামিক স্কলার)", "https://www.facebook.com/sheikhahmadullahofficial/", "https://www.youtube.com/@sheikhahmadullahofficial"),
                new Person("কথা মাসুদ কামাল (সাংবাদিক)", "", "https://www.youtube.com/@kothamasoodkamal"),
                new Person("আসিফ মাহতাব উত্শা (ইউটিউবার)", "", "https://www.youtube.com/c/AsifMahtabUtsha?app=desktop"),
                new Person("ফাহাম আবদুস সালাম (রাজনীতিবিদ)", "https://www.facebook.com/faham.abdus/", "https://www.youtube.com/@fahamabdussalam3164"),
                new Person("শাহেদ চৌধুরী (সাংবাদিক)", "https://www.facebook.com/shahedjournalist", "https://www.youtube.com/@shahedjournalist"),
                new Person("রাব্বি খান (ইউটিউবার)", "https://www.facebook.com/rabbikhan.official", "https://www.youtube.com/@Rabbikhan"),
                new Person("সাবরিনা সুলতানা (সাংবাদিক)", "https://www.facebook.com/sabrina.sultana.bd", "https://www.youtube.com/@SabrinaSultana"),
                new Person("তানভীর হাসান (ইউটিউবার)", "https://www.facebook.com/tanvirhasan.official", "https://www.youtube.com/@TanvirHasan"),
                new Person("সাইফুল ইসলাম (সাংবাদিক)", "https://www.facebook.com/saifulislam.bd", "https://www.youtube.com/@FaceThePeople"),
                new Person("গোলাম মাওলা রনি (রাজনীতিবিদ/এমপি)", "", "https://www.youtube.com/@GolamMaulaRonyMP")
                // এখানে আরও নাম যোগ করতে থাকুন
        };

        for (Person p : prominentPeople) {
            CardView card = (CardView) getLayoutInflater().inflate(R.layout.item_prominent_person, container, false);
            ((TextView) card.findViewById(R.id.personNameTextView)).setText(p.name);

            TextView fbText = card.findViewById(R.id.facebookLinkTextView);
            ImageView fbIcon = card.findViewById(R.id.facebookIcon);
            View.OnClickListener fbClickListener = v -> {
                String fbUrl = p.facebook;
                // যদি fb://profile/ID দিয়ে শুরু হয়, তাহলে কনভার্ট করো
                if (fbUrl != null && fbUrl.startsWith("fb://profile/")) {
                    String profileId = fbUrl.substring("fb://profile/".length());
                    if (profileId.contains("?")) {
                        profileId = profileId.substring(0, profileId.indexOf("?"));
                    }
                    fbUrl = "https://www.facebook.com/profile.php?id=" + profileId;
                }
                // যদি লিংকের শেষে /profile থাকে, সেটি কেটে দাও
                if (fbUrl != null && fbUrl.endsWith("/profile")) {
                    fbUrl = fbUrl.substring(0, fbUrl.length() - "/profile".length());
                }
                Toast.makeText(this, "ওয়েবসাইট লোড হচ্ছে...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", fbUrl);
                startActivity(intent);
            };
            fbText.setOnClickListener(fbClickListener);
            fbIcon.setOnClickListener(fbClickListener);

            TextView ytText = card.findViewById(R.id.youtubeLinkTextView);
            ytText.setOnClickListener(v -> {
                Toast.makeText(this, "ওয়েবসাইট লোড হচ্ছে...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", p.youtube);
                startActivity(intent);
            });

            container.addView(card);
        }
    }

} 