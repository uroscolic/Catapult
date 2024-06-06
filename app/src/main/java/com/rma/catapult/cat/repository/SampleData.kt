package com.rma.catapult.cat.repository

import com.rma.catapult.cat.domain.CatInfo
import com.rma.catapult.cat.domain.Weight
import com.rma.catapult.cat.list.api.model.CatListUiModel

val SampleData = listOf(
    CatInfo(
        id = "1",
        name = "Crna",
        alt_names = "Black Black",
        description = "Crna macka, mala i slatka. Obozava da se mazi i da se igra. Obozava da jede ribu. Veoma je nezavisna i voli da se igra lopticom. Obozava da se mazi i da se igra. Obozava da jede ribu. Veoma je nezavisna i voli da se igra lopticom. Obozava da se mazi i da se igra. Obozava da jede ribu. Veoma je nezavisna i voli da se igra lopticom. Obozava da se mazi i da se igra. Obozava da jede ribu. Veoma je nezavisna i voli da se igra lopticom.",
        origin = "Srbija",
        temperament = "Ljubazna, Nezavisna, Osetljiva, Prijateljska, Samostalna",
        life_span = "10 godina, 11 meseci, 12 dana",
        weight = Weight("5kg", "2kg"),
        adaptability = 3,
        affection_level = 5,
        child_friendly = 4,
        dog_friendly = 2,
        energy_level = 1,
        rare = 0,
        wikipedia_url = "https://en.wikipedia.org/wiki/Black_cat"
    ),
    CatInfo(
        id = "2",
        name = "Bela",
        alt_names = "White",
        description = "Mala bela macka",
        origin = "Srbija",
        temperament = "Bezobrazna, Inteligentna, Ljubazna, Ljubomorna, Nezavisna, Osetljiva, Oštra, Prijateljska, Samostalna",
        life_span = "14 godina",
        weight = Weight("5kg", "2kg"),
        adaptability = 2,
        affection_level = 5,
        child_friendly = 1,
        dog_friendly = 2,
        energy_level = 4,
        rare = 1,
        wikipedia_url = "https://en.wikipedia.org/wiki/White_cat"
    ),
    CatInfo(
        id = "3",
        name = "Siva",
        alt_names = "Grey",
        description = "Mala siva maca",
        origin = "Srbija",
        temperament = "Ljubazna, Nezavisna, Osetljiva, Prijateljska, Samostalna",
        life_span = "15 godina",
        weight = Weight("5kg", "2kg"),
        adaptability = 5,
        affection_level = 5,
        child_friendly = 1,
        dog_friendly = 1,
        energy_level = 5,
        rare = 0,
        wikipedia_url = "https://en.wikipedia.org/wiki/Grey_cat"
    ),
)
val SampleDataUiModel = listOf(
    CatListUiModel(
        id = "1",
        name = "Crna",
        alt_names = "Black",
        description = "Crna macka, mala i slatka. Obozava da se mazi i da se igra. Obozava da jede ribu. Veoma je nezavisna i voli da se igra lopticom. Obozava da se mazi i da se igra. Obozava da jede ribu. Veoma je nezavisna i voli da se igra lopticom. Obozava da se mazi i da se igra. Obozava da jede ribu. Veoma je nezavisna i voli da se igra lopticom. Obozava da se mazi i da se igra. Obozava da jede ribu. Veoma je nezavisna i voli da se igra lopticom.",
        temperament = "Ljubazna, Nezavisna, Osetljiva, Prijateljska, Samostalna",
    ),
    CatListUiModel(
        id = "2",
        name = "Bela",
        alt_names = "White",
        description = "Mala bela macka",
        temperament = "Bezobrazna, Inteligentna, Ljubazna, Ljubomorna, Nezavisna, Osetljiva, Oštra, Prijateljska, Samostalna",
    ),
    CatListUiModel(
        id = "3",
        name = "Siva",
        alt_names = "Grey",
        description = "Mala siva maca",
        temperament = "Ljubazna, Nezavisna, Osetljiva, Prijateljska, Samostalna",
    ),
)