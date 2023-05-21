package com.geshanzsq.nav.utils;

import com.geshanzsq.nav.domain.UserSiteHits;
import com.geshanzsq.nav.mapper.UserSiteHitsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ItemBasedCF {
    private int n_sim_site = 20;
    private int n_rec_site = 8;
    private Map<String, Map<String, String>> trainSet;
    private Map<String, Map<String, String>> testSet;
    private Map<String, Map<String, Double>> site_sim_matrix;
    private Map<String, Integer> site_popular;
    private int site_count;

    @Autowired
    private UserSiteHitsMapper userSiteHitsMapper;


    public void getDataset() {
        double pivot = 0.75;
        trainSet = new HashMap<>();
        testSet = new HashMap<>();

        int trainSet_len = 0;
        int testSet_len = 0;

        List<UserSiteHits> lists = userSiteHitsMapper.findAllUserSiteHits();

        //遍历lists列表，并存储数据

        for(UserSiteHits ust : lists){
            String user = ust.getUserId()+"";
            String site = ust.getSiteId() +"";
            String hits = ust.getHits() +"";

            if (Math.random() < pivot) {
                trainSet.putIfAbsent(user, new HashMap<>());
                trainSet.get(user).put(site, hits);
                trainSet_len++;
            } else {
                testSet.putIfAbsent(user, new HashMap<>());
                testSet.get(user).put(site, hits);
                testSet_len++;
            }
        }
    }

    public void calcMovieSimilarity() {
        site_sim_matrix = new HashMap<>();
        site_popular = new HashMap<>();

        for (Map<String, String> sites : trainSet.values()) {
            for (String site : sites.keySet()) {
                site_popular.put(site, site_popular.getOrDefault(site, 0) + 1);
            }
        }

        site_count = site_popular.size();

        for (Map<String, String> sites : trainSet.values()) {
            for (String m1 : sites.keySet()) {
                for (String m2 : sites.keySet()) {
                    if (m1.equals(m2)) {
                        continue;
                    }

                    site_sim_matrix.putIfAbsent(m1, new HashMap<>());
                    site_sim_matrix.get(m1).putIfAbsent(m2, 0.0);
                    site_sim_matrix.get(m1).put(m2, site_sim_matrix.get(m1).get(m2) + 1);
                }
            }
        }

        for (Map.Entry<String, Map<String, Double>> entry : site_sim_matrix.entrySet()) {
            String m1 = entry.getKey();
            Map<String, Double> related_sites = entry.getValue();
            for (Map.Entry<String, Double> related_entry : related_sites.entrySet()) {
                String m2 = related_entry.getKey();
                double count = related_entry.getValue();

                if (site_popular.get(m1) == 0 || site_popular.get(m2) == 0) {
                    site_sim_matrix.get(m1).put(m2, 0.0);
                } else {
                    site_sim_matrix.get(m1).put(m2, count / Math.sqrt(site_popular.get(m1) * site_popular.get(m2)));
                }
            }
        }
    }
    public List<Map.Entry<String, Double>> recommend(String user) {
        int K = n_sim_site;
        int N = n_rec_site;
        Map<String, Double> rank = new HashMap<>();
        Map<String, String> watched_sites = trainSet.get(user);

        for (Map.Entry<String, String> entry : watched_sites.entrySet()) {
            String site = entry.getKey();
            String rating = entry.getValue();

            for (Map.Entry<String, Double> related_entry : site_sim_matrix.get(site).entrySet()) {
                String related_site = related_entry.getKey();
                double w = related_entry.getValue();

                if (watched_sites.containsKey(related_site)) {
                    rank.put(related_site,0.0);
                }

                rank.putIfAbsent(related_site, 0.0);
                rank.put(related_site, rank.get(related_site) + w * Double.parseDouble(rating));
            }
        }

        List<Map.Entry<String, Double>> sortedRank = new ArrayList<>(rank.entrySet());
        sortedRank.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        return sortedRank.subList(0, Math.min(N, sortedRank.size()));
    }

    public void evaluate() {
        System.out.println("Evaluating start ...");
        int N = n_rec_site;
        int hit = 0;
        int rec_count = 0;
        int test_count = 0;
        Set<String> all_rec_sites = new HashSet<>();

        for (Map.Entry<String, Map<String, String>> entry : trainSet.entrySet()) {
            String user = entry.getKey();
            Map<String, String> test_sites = testSet.getOrDefault(user, Collections.emptyMap());
            List<Map.Entry<String, Double>> rec_sites = recommend(user);

            for (Map.Entry<String, Double> rec_entry : rec_sites) {
                String site = rec_entry.getKey();
                all_rec_sites.add(site);

                if (test_sites.containsKey(site)) {
                    hit++;
                }
            }

            rec_count += N;
            test_count += test_sites.size();
        }

        double precision = (double) hit / rec_count;
        double recall = (double) hit / test_count;
        double coverage = (double) all_rec_sites.size() / site_count;

        System.out.printf("precision=%.4f\trecall=%.4f\tcoverage=%.4f\n", precision, recall, coverage);
    }


}
