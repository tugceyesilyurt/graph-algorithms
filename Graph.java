/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Adjacency Matrix representation in Java

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    private int adjMatrix[][]; // her 2 şehir arasındaki kenarların ağırlıklarını ve ilişkilerini depolar.
                               //Matrisin [i][j] hücresindeki değer, şehir i ile şehir j arasındaki kenarın ağırlığını gösterir.
    private String cities[];   //: Bu, şehir isimlerini depolayan bir dizedir. 
    private int numVertices;   // toplam düğüm (şehir) sayısını 


    // Initialize the matrix and hash table
    public Graph(int numVertices) {
        this.numVertices = numVertices;  //grafikteki toplam düğüm sayısını belirler.
        adjMatrix = new int[numVertices][numVertices]; // ADJmatrisini temsil eden iki boyutlu tamsayı dizisini oluşturur.
        //numVertices sayısı, matrisin boyutunu belirler.
        //Matrisin her bir hücresi, grafikteki iki düğüm arasındaki kenarın ağırlığını depolar.

        cities = new String[353];  //şehir isimlerini depolayan bir dizi 
        for(int i=0;i<cities.length;i++){
            cities[i]="";  //cities dizisini boş bir şekilde başlatır. Yani, her elemanı boş bir karakter dizisi olarak ayarlar. 
            //Bu, daha sonra şehir isimlerini bu diziye eklemek için kullanılacak olan bir hash tablosu işlemidir.
        }
    }

    // şehir ismini hashler ve  ismi  indekse dönüştürür.
    public int hashFunction(String key) {   
        String keyStr = key.toString(); // Verilen şehir ismini bir karakter dizisine dönüştürür ASCII değer için
        int hash = 0;    //Başlangıçta hash değerini sıfır olarak ayarlar
        for (int i = 0; i < keyStr.length(); i++) {
            hash = 31 * hash + keyStr.charAt(i);
        }         // Her karakterin ASCII değeri ile  hash değeri çarpılır ve yeni karakterin ASCII değeri eklenir

        return Math.abs(hash) % numVertices; // (grafikteki düğüm sayısı) ile mod alınarak bir indeks elde edilir.
        //Bu indeks, hash tablosundaki bu şehirin konumunu temsil eder.
    }


    public void setIndex(String str){
        int hash = hashFunction(str);    //  şehir ismini hash fonksiyonu ile bir indekse dönüştürür ve hash adlı değişkende saklar

            // Eğer hash tablosundaki bu indekste şehir ismi boş değilse ve farklı bir isimle doluysa
        if(!cities[hash].equals("") && !cities[hash].equals(str)){
            for(int i=0;i<cities.length;i++){   // Çakışma (collision) durumunu çözmek için bir döngü başlatır

                if(cities[i].equals(str)){     // Eğer şehir ismi zaten varsa, indeksi günceller ve döngüden çıkar

                    hash=i;
                    break;
                }
                if(cities[i].equals("")){   // Eğer boş bir indeks bulunursa, indeksi günceller ve döngüden çıkar

                    hash=i;
                    break;
                }
            }
        }
        cities[hash]=str;    // Hash tablosundaki uygun indekse şehir ismini ekler.Eğer çakışma olmuşsa, uygun bir indeks bulup ekler.

    }

    public int getIndex(String str){  // cities dizisinde dolaşarak verilen şehir ismini arar
        for(int i=0;i< cities.length; i++){  // Eğer şehir ismi bulunursa, o anki indeksi geri döndürür
            if(cities[i].equals(str)){   // Şehir ismi bulunamazsa, -1 değerini geri döndürür
                return i;
            }
        }

        return -1;
    }

    // start) ve bitiş (end) şehir isimlerini kullanarak bu şehirlerin dizideki indekslerini bulur
    public void addEdge(String start, String end, int weight) {
        int i = getIndex(start);
        int j = getIndex(end);
        adjMatrix[i][j] = weight;  // adjMatrix[i][j] hücresine kenarın ağırlığını atar


    }

    // Remove edges
    public void removeEdge(String start, String end) {
        int i = getIndex(start);
        int j = getIndex(end);
        adjMatrix[i][j] = 0;
    }

    public void readGraphFromFile() {
        String filePath = "graph.txt";

       // Dosyayı oku ve grafik oluştur
        try {
            File file = new File(filePath); // Dosya nesnesi oluştur
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {   // Dosyadaki her satırı oku
                String line = scanner.nextLine();  // Satırı al
                String[] parts = line.split(" -> ");   // Satırı " -> " işaretine göre ayır
                String startNode = parts[0];   // Başlangıç düğümünü al.

                String[] edges = parts[1].split(", ");   // Kenar bilgilerini içeren kısmı ayır
                for (int i=0; i<edges.length; i++) {
                    String[] edgeParts = edges[i].split(": ");
                    String endNode = edgeParts[0];  // Bitiş düğümünü al.
                    int weight = Integer.parseInt(edgeParts[1]);  // Kenarın ağırlığını inTe e çevir
                    this.setIndex(startNode);     // Başlangıç ve bitiş düğümlerini hash tablosuna ekle
                    this.setIndex(endNode);
                    this.addEdge(startNode, endNode, weight);   // Kenarı grafik üzerine ekle
                }
            }

            scanner.close();
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);   // Dosya bulunamazsa RuntimeException fırlat
        }


    }

    public boolean IsThereAPath(String v1, String v2) {
        int startIdx = getIndex(v1);
        int endIdx = getIndex(v2); // Başlangıç ve bitiş şehirlerinin dizideki indekslerini bul

        if (startIdx == -1 || endIdx == -1) {  // Başlangıç veya bitiş şehiri grafikte yoksa false döndür
            // Either v1 or v2 is not in the graph
            return false;
        }

             // DFS kullanarak bir yolun olup olmadığını kontrol et
        boolean[] visited = new boolean[numVertices];
        return DFS(startIdx, endIdx, visited);   }

    private boolean DFS(int startIdx, int endIdx, boolean[] visited) {
        // // Şu anki düğümü ziyaret edilmiş olarak işaretle
        visited[startIdx] = true;

        //  // Eğer hedef düğüme ulaşıldıysa, true döndür
        if (startIdx == endIdx) { //  Eğer şu anki düğüm hedef düğüme eşitse, true döndür. Yani, bir yol bulunmuştur.
            return true;
        }

        //  // Şu anki düğüme bitişik olan tüm düğümleri tekrarla
        for (int i = 0; i < numVertices; i++) {  //  Şu anki düğüme bitişik olan tüm düğümleri kontrol etmek için bir döngü başlat.
            if (adjMatrix[startIdx][i] != 0 && !visited[i]) { // Eğer iki düğüm arasında bir kenar var ve iki düğüm henüz ziyaret edilmemişse
                if (DFS(i, endIdx, visited)) { // DFS'yi rekürsif olarak çağır ve eğer bu rekürsif çağrı true döndürüyorsa, yani bir yol bulunduysa, true döndür.
                    return true;
                }
            }
        }

        // If no path is found, return false
        return false;
    }

    public void BFSfromTo(String v1, String v2) { // Başlangıç ve bitiş şehirlerinin dizideki indekslerini bul
        int startIdx = getIndex(v1);
        int endIdx = getIndex(v2);

        if (startIdx == -1 || endIdx == -1) {     // Başlangıç veya bitiş şehiri grafikte yoksa hata mesajı yazdır ve metoddan çık

            System.out.println("Invalid vertices");
            return;
        }

        boolean[] visited = new boolean[numVertices];      // BFS algoritması için kullaNACAĞIMIZ  ziyaret edilmiş düğümleri ve parent listesini oluştur

        int[] parent = new int[numVertices];

        Queue<Integer> queue = new LinkedList<>();      // BFS için kullanılacak kuyruk (queue) oluştur ve başlangıç düğümünü kuyruğa ekle

        queue.add(startIdx);
        visited[startIdx] = true;      // Başlangıç düğümünü ziyaret edildi olarak işaretle, parent'ını -1 olarak belirle

        parent[startIdx] = -1;

        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();  // Kuyruktan bir düğümü çıkar

            if (currentVertex == endIdx) {          // Eğer çıkarılan düğüm hedef düğüme eşitse, en kısa yol bulundu, yazdır ve metoddan çık

                // Found the destination vertex, print the path
                System.out.println("Sequence of vertices and edges(BFS) from " + cities[startIdx] + " to " + cities[endIdx] + ":");
                printPath(startIdx, endIdx, parent);
                System.out.println();
                return;
            }
                   // Çıkarılan düğüme bitişik olan tüm düğümleri kontrol et

            for (int i = 0; i < numVertices; i++) {              // Eğer iki düğüm arasında bir kenar var ve iki düğüm henüz ziyaret edilmemişse

                if (adjMatrix[currentVertex][i] != 0 && !visited[i]) {
                    queue.add(i);                  // Bu düğümü kuyruğa ekle, ziyaret edildi olarak işaretle ve parent'ını belirle

                    visited[i] = true;
                    parent[i] = currentVertex;
                }
            }
        }

        // No path found
        System.out.println("No path found between " + v1 + " and " + v2);      // Kuyruk boşaldığı halde hedef düğüme ulaşılamamışsa, yol bulunamadı mesajını yazdır

    }

    public void DFSfromTo(String v1, String v2) {
        int startIdx = getIndex(v1);      // Başlangıç ve bitiş şehirlerinin dizideki indekslerini bul

        int endIdx = getIndex(v2);

        if (startIdx == -1 || endIdx == -1) {      // Başlangıç veya bitiş şehiri grafikte yoksa hata mesajı yazdır ve metoddan çık

            System.out.println("Invalid vertices");
            return;
        }

        boolean[] visited = new boolean[numVertices];      // DFS algoritması için kullanılacak olan ziyaret edilmiş düğüm listesini ve parent listesini oluştur

        int[] parent = new int[numVertices];

        // Stack to perform DFS
        Stack<Integer> stack = new Stack<>();      // DFS algoritması için kullanılacak stack'i oluştur, başlangıç düğümünü stack'e ekle, başlangıç düğümünü ziyaret edilmiş olarak işaretle ve parent'ını -1 olarak belirle

        stack.push(startIdx);
        visited[startIdx] = true;//. Başlangıç düğümü stack'e eklenir ve bu düğüm ziyaret edilmiş olarak işaretlenir. Parent değeri -1 olarak belirlenir, 
        //çünkü başlangıç düğümüne giden bir önceki düğüm yoktur (kök düğüm). 
        parent[startIdx] = -1;

        while (!stack.isEmpty()) {
            int currentVertex = stack.pop();           // Stack'ten bir düğümü çıkar


            if (currentVertex == endIdx) {           // Eğer çıkarılan düğüm hedef düğüme eşitse, yol bulundu, yazdır ve metoddan çık

                // Found the destination vertex, print the path
                System.out.println("Sequence of vertices and edges(DFS) from " + cities[startIdx] + " to " + cities[endIdx] + ":");
                printPath(startIdx, endIdx, parent);
                System.out.println();
                return;
            }

            for (int i = 0; i < numVertices; i++) {         // Çıkarılan düğüme bitişik olan tüm düğümleri kontrol et

                if (adjMatrix[currentVertex][i] != 0 && !visited[i]) {              // Eğer iki düğüm arasında bir kenar var ve iki düğüm henüz ziyaret edilmemişse

                    stack.push(i);
                    visited[i] = true;                  // Bu düğümü stack'e ekle, ziyaret edildi olarak işaretle ve parent'ını belirle

                    parent[i] = currentVertex;
                }
            }
        }

        // No path found      // Stack boşaldığı halde hedef düğüme ulaşılamamışsa, yol bulunamadı mesajını yazdır

        System.out.println("No path found between " + v1 + " and " + v2);
    }

    public int NumberOfSimplePaths(String v1, String v2) {
        int startIdx = getIndex(v1);
        int endIdx = getIndex(v2);

        if (startIdx == -1 || endIdx == -1) {
            System.out.println("Invalid vertices");
            return 0;
        }

        boolean[] visited = new boolean[numVertices];     // Basit yolları saymak için kullanılacak olan ziyaret edilmiş düğüm listesini oluştur

        return countSimplePaths(startIdx, endIdx, visited);      // countSimplePaths metodu ile basit yolları say ve sonucu döndür

    }

    private int countSimplePaths(int currentVertex, int endIdx, boolean[] visited) {
        // If the current vertex is the destination, there is one path
        if (currentVertex == endIdx) {      // Eğer mevcut düğüm hedef düğüme eşitse, bir basit yol bulundu, 1 döndür

            return 1;
        }

        visited[currentVertex] = true;

        int countPaths = 0;

        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[currentVertex][i] != 0 && !visited[i]) {          // Eğer iki düğüm arasında bir kenar var ve henüz ziyaret edilmemişse

                countPaths += countSimplePaths(i, endIdx, visited);     // countSimplePaths metodunu rekürsif olarak çağır ve dönen değeri toplama ekle

            }
        }

        visited[currentVertex] = false; // Backtrack       // Ziyaret edilen düğümü geri al (backtrack)


        return countPaths;
    }

    public List<String> Neighbors(String v1) {
        int index = getIndex(v1);  // Verilen düğümün dizideki indeksini bul

        if (index == -1) {      // Eğer düğüm dizide bulunamazsa (yoksa), hata mesajı yazdır ve null döndür

            System.out.println("Invalid vertex");
            return null;
        }

        List<String> neighborsList = new ArrayList<>();      // Komşuları tutmak için bir liste oluştur


        for (int i = 0; i < numVertices; i++) {     // Tüm düğümleri kontrol et

            if (adjMatrix[index][i] != 0) {          // Eğer verilen düğüm ile i arasında bir kenar varsa 0 değilse

                neighborsList.add(cities[i]);              // Komşu düğümü listeye ekle

            }
        }

        return neighborsList;
    }

    public List<String> HighestDegree() {
        int maxDegree = -1;
        List<String> verticesWithHighestDegree = new ArrayList<>(); //String  içeren bir liste  , şehir isimlerini bu listede tutabilirsiniz

        for (int i = 0; i < numVertices; i++) {
            int degree = calculateDegree(i);          // Düğümün derecesini hesapla


            if (degree > maxDegree) {          // Eğer hesaplanan derece, şu ana kadar bulunan en yüksek dereceden büyükse

                maxDegree = degree;              // En yüksek dereceyi güncelle

                verticesWithHighestDegree.clear();              // Düğümleri içeren listeyi temizle ve yeni düğümü ekle

                verticesWithHighestDegree.add(cities[i]);
            } else if (degree == maxDegree) {             // Eğer hesaplanan derece, şu ana kadar bulunan en yüksek dereceye eşitseekle
                verticesWithHighestDegree.add(cities[i]);
            }
        }

        return verticesWithHighestDegree;
    }

    private int calculateDegree(int vertexIndex) {
        int degree = 0;

        for (int i = 0; i < numVertices; i++) {   // Belirli bir düğümün tüm komşularını kontrol et
         // Eğer belirli bir düğüm ile i arasında bir kenar varsa

            if (adjMatrix[vertexIndex][i] != 0) {
                degree++;             // Dereceyi bir artır

            }
        }

        return degree;
    }

    public boolean IsDirected() {
        for (int i = 0; i < numVertices; i++) {     // Tüm matris elemanlarını kontrol et

            for (int j = 0; j < numVertices; j++) {
                if (adjMatrix[i][j] != adjMatrix[j][i]) {              // Eğer matrisin i,j ve j,i elemanları eşit değilse (asimetri varsa)  the graph is directed

                    return true;                  // Grafiğin yönlendirilmiş olduğunu belirtmek için true döndür

                }
            }
        }
        return false;     // Matrisin tüm elemanları simetrik olduğu için, grafiğin yönlendirilmemiş olduğunu belirtmek için false döndür

    }

    public boolean AreTheyAdjacent(String v1, String v2) {     

        int index1 = getIndex(v1);
        int index2 = getIndex(v2);  // Verilen düğümlerin indekslerini al
             
        if (index1 == -1 || index2 == -1) {     // Eğer bir veya her iki düğüm de geçerli değilse

            System.out.println("Invalid vertices");
            return false;
        }

        return adjMatrix[index1][index2] != 0;   // Eğer belirli düğümler arasında bir kenar varsa, komşu olduklarını belirtmek için true döndür

    }

    public boolean IsThereACycle(String v1) {
        int startIdx = getIndex(v1);     // Başlangıç düğümünün indeksini al


        if (startIdx == -1) {      // Eğer başlangıç düğümü geçerli değilse

            System.out.println("Invalid vertex");
            return false;
        }

        boolean[] visited = new boolean[numVertices];      // Ziyaret edilen düğümleri takip etmek için bir boolean dizisi oluşturve

        List<Integer> path = new ArrayList<>();      // Döngüyü kontrol etmek için bir liste oluştur


        return hasCycleDFS(startIdx, startIdx, visited, path);      // hasCycleDFS metodunu kullanarak çevrim kontrolü yap

    }

    private boolean hasCycleDFS(int currentVertex, int startVertex, boolean[] visited, List<Integer> path) {
        visited[currentVertex] = true;     // Mevcut düğümü ziyaret edildi olarak işaretle ve yolu güncelle

        path.add(currentVertex); //Mevcut düğümü ziyaret edildi olarak işaretler ve geçici bir liste olan path listesine ekler.

        for (int i = 0; i < numVertices; i++) {     // Komşu düğümleri kontrol et

            if (adjMatrix[currentVertex][i] != 0) {   // Eğer iki düğüm arasında bir kenar varsa    
                if (!visited[i]) {                 // Eğer komşu düğüm ziyaret edilmediyse


                    if (hasCycleDFS(i, startVertex, visited, path)) {  // Rekürsif olarak DFS çağır
                        return true;
                    }
                } else if (i == startVertex) {
                     //Eğer komşu düğüm zaten ziyaret edilmişse ve bu düğüm başlangıç düğümüne eşitse, bir çevrim bulunmuştur ve 
                    printCyclePath(path); //printCyclePath metodunu çağırarak çevrim yolu yazdırılır.
                    return true;
                }
            }
        }

        //       // : Mevcut düğümü ziyaret edilmemiş olarak işaretle ve yolu geri al
        visited[currentVertex] = false;
        path.remove(path.size() - 1);

        return false;
    }

    private void printCyclePath(List<Integer> path) {
        System.out.print("Cycle Path: ");
        for (int vertex : path) {  // Çevrimin yolu üzerindeki her düğümü (vertex) al ve bu düğümün şehir ismini (cities[vertex]) ekrana yazdır. Her düğüm arasına bir boşluk bırak.
            System.out.print(cities[vertex] + " ");          // Her düğümün şehir ismini al ve ekrana yazdır, düğümler arasına bir boşluk bırak

        }
        System.out.println();
    }

    public void NumberOfVerticesInComponent(String v1) {// belirli bir düğümün içerisinde bulunduğu bileşendeki düğüm sayısını hesaplar
        int startIdx = getIndex(v1);      // Verilen düğümün indisini al


        if (startIdx == -1) {     // Eğer geçersiz bir düğüm indisine sahipse, hata mesajı yazdır ve metodu sonlandır

            System.out.println("Invalid vertex");
            return;
        }

        boolean[] visited = new boolean[numVertices];  // Ziyaret edilen düğümleri takip etmek için bir boolean dizisi oluştur

        int componentSize = countComponentVertices(startIdx, visited);     // Belirli bir düğümden başlayarak içinde bulunduğu bileşendeki düğüm sayısını hesapla


        System.out.println("Number of vertices in the component containing " + v1 + ": " + componentSize);
    }

    private int countComponentVertices(int currentVertex, boolean[] visited) {
        visited[currentVertex] = true;      // Şu anki düğümü ziyaret edildi olarak işaretle

        int componentSize = 1;     // Bileşendeki düğüm sayısını 1 olarak başlat
                 //döngüyle  mevcut düğüm ile diğer düğümler arasındaki kenarları kontrol eder.
        for (int i = 0; i < numVertices; i++) {         // Eğer i ile currentVertex arasında bir kenar varsa ve i ziyaret edilmemişse

            if (adjMatrix[currentVertex][i] != 0 && !visited[i]) {             // countComponentVertices metodunu tekrar çağırarak bağlı olduğu bileşendeki diğer düğümleri say

                componentSize += countComponentVertices(i, visited);
            }
        }

        return componentSize;
    }



    public void WhatIsShortestPathLength(String v1, String v2) {
        int startIdx = getIndex(v1);
        int endIdx = getIndex(v2);

        if (startIdx == -1 || endIdx == -1) {     // Eğer başlangıç veya bitiş şehiri grafikte bulunmuyorsa, hata mesajı yazdır ve metoddan çık

            System.out.println("Invalid vertices");
            return;
        }

        List<List<String>> allPaths = new ArrayList<>();      // Tüm yolları içeren bir liste ve mevcut yolu tutacak bir liste oluştur

        List<String> currentPath = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];      // Ziyaret durumunu tutan bir dizi oluştur


        findShortestPaths(endIdx, startIdx, visited, currentPath, allPaths);     // En kısa yolları bulmak için findShortestPaths metodunu kullan


        if (allPaths.isEmpty()) { // Eğer hiç yol bulunamazsa
            System.out.println(v1 + " --x-- " + v2);
            return;
        }

        int shortestLength = Integer.MAX_VALUE;      // En kısa yol uzunluğunu hesapla

        for (List<String> path : allPaths) {
            int pathLength = calculatePathLength(path);
            if (pathLength < shortestLength) {
                shortestLength = pathLength;
            }
        }
        System.out.println("ALL PATHS FROM "+v1+" to "+v2+": "+allPaths);
        System.out.println("Shortest path length from " + v1 + " to " + v2 + ": " + shortestLength);
    }

    private void findShortestPaths(int endIdx, int currentIdx, boolean[] visited, List<String> currentPath, List<List<String>> allPaths) {
        visited[currentIdx] = true;
        currentPath.add(cities[currentIdx]);

        if (currentIdx == endIdx) {  // Eğer mevcut indeks hedef indekse eşitse, yolu tüm yollar listesine ekler. Eğer şu anki indeks hedef indekse ulaşıldıysa,
        // şu anki yolu bir kopya olarak alıp tüm yollar listesine ekler.
            List<String> newPath = new ArrayList<>(currentPath);
            allPaths.add(newPath);
        } else {          // Hedefe ulaşılmadıysa, mevcut şehirdeki tüm komşulara gidilir.

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[currentIdx][i] != 0 && !visited[i]) {  // Henüz ziyaret edilmemiş ve bağlantı varsa, yolun devamını aramak üzere
                // bu komşuya geçilir.
                    findShortestPaths(endIdx, i, visited.clone(), new ArrayList<>(currentPath), allPaths);
                }  //Mevcut şehir ile i arasında bir kenar varsa ve i henüz ziyaret edilmemişse, rekürsif olarak bu komşuya geçilir.
            }
        }
    }

    private int calculatePathLength(List<String> path) {
        int length = 0; A-B-C D ÖRNEĞİN
        for (int i = 0; i < path.size() - 1; i++) { // Şehir listesinde dolaşılır (birinci şehirden başlayarak, sonuncu şehire kadar)
            int fromIdx = getIndex(path.get(i));  //Mevcut şehirin dizinini (getIndex metodunu kullanarak) bulur.
            int toIdx = getIndex(path.get(i + 1));  // Bir sonraki şehirin dizinini (getIndex metodunu kullanarak) bulur.
            length += adjMatrix[fromIdx][toIdx];  //Bu iki şehir arasındaki kenar ağırlığını toplam uzunluğa ekler.
        }
        return length;
    }



    private void printPath(int startIdx, int endIdx, int[] parent) {
        if (startIdx == endIdx) {  //Eğer başlangıç düğümü ve bitiş düğümü aynı ise, sadece başlangıç düğümünün şehir ismini ekrana yazdır.
            System.out.print(cities[startIdx]);
        } else {
            printPath(startIdx, parent[endIdx], parent);
            System.out.print(" -> " + cities[endIdx]);
        }  //: Eğer başlangıç ve bitiş düğümleri farklı ise, başlangıç düğümünden başlayarak bir önceki düğüme kadar olan yolu (printPath metodunu rekürsif olarak çağırarak) ekrana yazdır. Sonra bir önceki düğümden başlayarak bitiş düğümüne kadar olan yolu ekrana yazdır. Her iki yol arasına " -> " eklenerek bağlantı gösterilir.

    }



}
