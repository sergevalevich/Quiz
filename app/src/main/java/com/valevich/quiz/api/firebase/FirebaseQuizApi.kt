package com.valevich.quiz.api.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.valevich.quiz.api.QuizApi
import com.valevich.quiz.flows.categories.entity.QuizCategory
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class FirebaseQuizApi(
        private val databaseReference: DatabaseReference
) : QuizApi {

    private val categoriesSubject: BehaviorSubject<List<QuizCategory>> = BehaviorSubject.create()
    private val categoriesUpdateSubject: BehaviorSubject<QuizCategory> = BehaviorSubject.create()

    override fun quizCategories(): Observable<List<QuizCategory>> {
        listenForCategories()
        return categoriesSubject.share()
    }

    override fun updateAnswers(category: QuizCategory): Observable<QuizCategory> {
        listenForCategotyUpdate(category)
        return categoriesUpdateSubject.share()
    }

    private fun listenForCategotyUpdate(category: QuizCategory) {
        databaseReference.child("quizCategories/${category.id}")
                .setValue(category)
                .addOnCompleteListener {
                    categoriesUpdateSubject.onNext(category)
                }
                .addOnFailureListener {
                    categoriesUpdateSubject.onError(it)
                }
    }

    private fun listenForCategories() {
        databaseReference.child("quizCategories")
                .orderByKey()
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        categoriesSubject.onNext(
                                dataSnapshot.children
                                        .mapNotNull { it.getValue(QuizCategory::class.java) }
                        )
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        categoriesSubject.onError(databaseError.toException())
                    }

                })
    }
}
