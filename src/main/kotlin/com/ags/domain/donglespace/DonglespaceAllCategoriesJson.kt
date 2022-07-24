package com.ags.domain.donglespace

import io.quarkus.runtime.annotations.RegisterForReflection

// all data classes here need to be `var` so it works on native mode :(
@RegisterForReflection
data class DonglespaceAllCategoriesJson(
    var data: AllCategoriesData,
)

@RegisterForReflection
data class AllCategoriesData(
    var allCategories: Array<Category>,
)

@RegisterForReflection
data class Category(
    var code: String = "",
    var name: String = "",
)
