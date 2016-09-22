package com.androidhuman.firebase.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.androidhuman.firebase.auth.model.OptionalFirebaseUser;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

final class SignInWithCredentialOnSubscribe
        implements Observable.OnSubscribe<OptionalFirebaseUser> {

    private final FirebaseAuth instance;

    private final AuthCredential credential;

    SignInWithCredentialOnSubscribe(FirebaseAuth instance, AuthCredential credential) {
        this.instance = instance;
        this.credential = credential;
    }

    @Override
    public void call(final Subscriber<? super OptionalFirebaseUser> subscriber) {
        final OnCompleteListener<AuthResult> listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(task.getException());
                    }
                    return;
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(
                            OptionalFirebaseUser.of(task.getResult().getUser()));
                    subscriber.onCompleted();
                }
            }
        };

        instance.signInWithCredential(credential)
                .addOnCompleteListener(listener);
    }
}