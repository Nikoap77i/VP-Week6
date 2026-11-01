package com.example.week6.data

import com.example.week6.data.model.*

object SampleData {

    val profile = StudentProfile(
        name = "Niko Adrian Purnomo",
        major = "Informatics AI • Angkatan 2025",
        nim = "NIM: 1076012510024",
        initials = "NAP"
    )

    val techOptions = listOf(
        "Kotlin", "Jetpack Compose", "Firebase", "Ktor", "Room", "Python", "Google Maps SDK"
    )

    val communities = listOf(
        Community(
            id = "gdsc", name = "GDSC", fullName = "Google Developer Student Club",
            tagline = "Google Developer Student Club", members = 142,
            description = "Fokus pada ekosistem Google: Android Compose, Flutter, Firebase, dan Google Cloud Platform.",
            icon = CommunityIcon.CODE, isJoined = true
        ),
        Community(
            id = "cybersec", name = "CyberSecurity Guild", fullName = "CyberSecurity Guild",
            tagline = "Security & Pentesting", members = 88,
            description = "Eksplorasi ethical hacking, CTF challenges, reverse engineering, dan pertahanan jaringan kampus.",
            icon = CommunityIcon.SECURITY, isJoined = true
        ),
        Community(
            id = "ai", name = "AI Society", fullName = "AI Society",
            tagline = "Machine Learning & LLM", members = 115,
            description = "Riset terapan deep learning, computer vision, agen cerdas, dan fine-tuning large language models.",
            icon = CommunityIcon.AI
        ),
        Community(
            id = "mobiledev", name = "Mobile Dev Community", fullName = "Mobile Dev Community",
            tagline = "Jetpack Compose & KMP", members = 95,
            description = "Native Jetpack Compose, Kotlin Multiplatform, dan pengembangan aplikasi iOS.",
            icon = CommunityIcon.MOBILE, isJoined = true
        ),
        Community(
            id = "cloud", name = "Cloud Native Club", fullName = "Cloud Native Club",
            tagline = "DevOps & Kubernetes", members = 64,
            description = "Belajar container, CI/CD, Kubernetes, dan arsitektur cloud-native bersama.",
            icon = CommunityIcon.CLOUD
        ),
        Community(
            id = "uiux", name = "UI/UX Guild", fullName = "UI/UX Guild",
            tagline = "Design & Prototyping", members = 73,
            description = "Riset pengguna, design system, prototyping Figma, dan kolaborasi desainer-developer.",
            icon = CommunityIcon.DESIGN
        )
    )

    val projects = listOf(
        Project(
            id = "proj-gdsc-01", communityId = "gdsc",
            title = "Smart Campus Navigation",
            description = "Sistem navigasi peta gedung kampus interaktif berbasis Jetpack Compose dan Google Maps SDK. " +
                "Memfasilitasi mahasiswa dan dosen menemukan ruang perkuliahan, laboratorium komputer, perpustakaan, " +
                "serta fasilitas umum kampus secara akurat.",
            techStack = listOf("Kotlin", "Jetpack Compose", "Room", "Google Maps SDK"),
            status = ProjectStatus.IN_PROGRESS
        ),
        Project(
            id = "proj-gdsc-02", communityId = "gdsc",
            title = "Student Attendance QR Scanner",
            description = "Pemindai barcode & QR presensi kehadiran workshop berbasis CameraX & Firebase.",
            techStack = listOf("Kotlin", "Jetpack Compose", "Firebase"),
            status = ProjectStatus.COMPLETED
        ),
        Project(
            id = "proj-cybersec-01", communityId = "cybersec",
            title = "CTF Scoreboard",
            description = "Papan skor real-time untuk kompetisi Capture The Flag antar mahasiswa.",
            techStack = listOf("Kotlin", "Ktor", "Python"),
            status = ProjectStatus.IN_PROGRESS
        ),
        Project(
            id = "proj-mobiledev-01", communityId = "mobiledev",
            title = "KMP Campus Events",
            description = "Aplikasi agenda kegiatan kampus lintas platform Android & iOS menggunakan Kotlin Multiplatform.",
            techStack = listOf("Kotlin", "Jetpack Compose", "Room"),
            status = ProjectStatus.IN_PROGRESS
        )
    )
}
