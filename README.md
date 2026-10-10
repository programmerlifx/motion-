# LIFX Motion Studio (Tahap 1)

Editor video Android native (Kotlin + Jetpack Compose + Room).

## Isi Tahap 1
- Tema dark premium (hitam kebiruan, ungu neon, cyan), ikon vektor konsisten (tanpa emoji)
- Ikon launcher: huruf L dan M menyatu dengan bentuk keyframe, gradasi ungu-cyan
- Beranda: buat proyek, cari, urutkan, buka, hapus, tampilan kosong
- Penyimpanan proyek lokal (Room) dan pembukaan kembali proyek
- Editor dasar: kanvas rasio 9:16 / 16:9 / 1:1 / 4:5, play/pause, playhead, grid, safe area

Belum ada (tahap berikutnya): impor media, timeline multi-layer, keyframe, efek, teks, audio, ekspor MP4, template.

## Build APK lewat GitHub
1. Buat repo baru di GitHub, lalu unggah semua isi folder ini (termasuk folder `.github`).
2. Buka tab **Actions**, pilih workflow **Build APK** (jalan otomatis saat push, atau klik **Run workflow**).
3. Setelah hijau, buka run tersebut, unduh artifact **LIFX-Motion-Studio-debug** (isinya `app-debug.apk`).
4. Pasang APK di HP (izinkan instal dari sumber tidak dikenal).

## Build lokal
Buka folder ini di Android Studio (Koala atau lebih baru, JDK 17), tunggu sync, lalu Run.
Atau dengan Gradle 8.7: `gradle assembleDebug`.

## Ikon SVG
Salinan SVG ada di `docs/icons/`. Versi di aplikasi adalah vector drawable di `app/src/main/res/drawable/`.
