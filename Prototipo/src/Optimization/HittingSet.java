/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Optimization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Igor Siqueira
 */
public class HittingSet {

    private class Teste implements Comparable {

        Integer node;
        Integer degree;

        public Teste(Integer node) {
            this.node = node;
            this.degree = 0;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Teste) {
                Teste t = (Teste) o;
                return degree.compareTo(t.degree);
            }
            throw new ClassCastException();
        }
    }

    public List<Teste> set = new ArrayList<>();
    public Set<Set<Integer>> collection = new HashSet<>();

    public HittingSet(Set<Integer> set) {
        for (Integer i : set) {
            this.set.add(new Teste(i));
        }
    }

    public Set<Integer> solve() {
        Set<Integer> minimumHittingSet = new HashSet<>();
        while (collection.size() > 0) {
            for (Teste t : set) {
                t.degree = 0;
                for (Set<Integer> subset : collection) {
                    if (subset.contains(t.node)) {
                        t.degree++;
                    }
                }
            }
            Collections.sort(set);
            Teste t = set.remove(set.size() - 1);
            minimumHittingSet.add(t.node);
            Object[] carray = collection.toArray();
            for (int i = carray.length - 1; i >= 0; i--) {
                if (((Set) carray[i]).contains(t.node)) {
                    collection.remove((Set) carray[i]);
                }
            }
        }

        return minimumHittingSet;
    }

}
