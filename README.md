Layihə Haqqında

Bu layihə Meta Graph API ilə inteqrasiya olunaraq istifadəçinin son postlarını götürür və onların performansını analiz edir.
Analiz zamanı postların engagement (like + comment) göstəriciləri hesablanır, ən yaxşı 3 post seçilir və günlər üzrə statistik analiz aparılır.

Funksionallıqlar
Meta Graph API-dan son 20 postun götürülməsi
Post məlumatlarının çıxarılması (mətn, tarix, like, comment)
Engagement hesablanması (like + comment)
Ən yüksək engagement-ə sahib 3 postun tapılması
Günlər üzrə like statistikası
Ümumi analitik nəticənin çıxarılması
API işləmədikdə mock data istifadəsi


İstifadə olunan texnologiyalar
Java 17+
Spring Boot
Spring Web (RestClient)
Meta Graph API
Gradle
REST API


Layihə strukturu
controller/   → API endpointlər
service/       → biznes məntiqi (API + analiz)
dto/           → məlumat modelləri

Environment (ENV) dəyişənləri

Layihənin kök hissəsində .env faylı yaradılır:

META_ACCESS_TOKEN=your_meta_access_token

application.yml faylında:

meta:
  access-token: ${META_ACCESS_TOKEN}

  İş prinsipi
Meta API-dan postlar alınır
Hər postdan lazımi məlumatlar çıxarılır
Engagement hesablanır
Ən yaxşı 3 post seçilir
Günlər üzrə like statistikası çıxarılır
Nəticə JSON formatında qaytarılır

