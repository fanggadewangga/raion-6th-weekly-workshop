package com.raion.weekly_workshop_6th.data.repository

import android.util.Log
import com.raion.weekly_workshop_6th.data.model.response.BaseResponse
import com.raion.weekly_workshop_6th.data.model.response.ErrorResponse
import com.raion.weekly_workshop_6th.data.model.response.NoteDetailData
import com.raion.weekly_workshop_6th.data.model.response.NoteListData
import com.raion.weekly_workshop_6th.data.utils.toDomain
import com.raion.weekly_workshop_6th.data.utils.toDomainList
import com.raion.weekly_workshop_6th.domain.model.Note
import com.raion.weekly_workshop_6th.domain.repository.NoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import java.io.File

class NoteRepositoryImpl(private val client: HttpClient) : NoteRepository {

    // Fetch all notes from the API
    override suspend fun getNotes(): Result<List<Note>> = try {
        val response: HttpResponse = client.get("api/notes") {
            accept(ContentType.Application.Json)
        }

        if (response.status == HttpStatusCode.OK) {
            // Parse response body into a typed data model
            val body = response.body<BaseResponse<NoteListData>>()
            Log.d("NoteRepository", "getNotes success: ${body.data.notes.size} items")
            Result.success(body.data.notes.toDomainList())
        } else {
            val error = response.body<ErrorResponse>()
            Log.e("NoteRepository", "getNotes failed with status: $error")
            Result.failure(Exception("Failed to fetch notes: ${error.error}"))
        }
    } catch (e: Exception) {
        Log.e("NoteRepository", "getNotes exception", e)
        Result.failure(e)
    }

    // Fetch a single note by its ID
    override suspend fun getNoteById(id: String): Result<Note> = try {
        val response: HttpResponse = client.get("api/notes/$id") {
            accept(ContentType.Application.Json)
        }

        if (response.status == HttpStatusCode.OK) {
            // Note is wrapped under `data.note` object
            // {
            //  "data": {
            //    "note": {
            //      "content": "string",
            //      "created_at": "string",
            //      "id": "string",
            //      "image_url": "string",
            //      "title": "string",
            //      "updated_at": "string",
            //      "user_id": "string"
            //    }
            //  },
            //  "message": "string",
            //  "status": "string"
            //}
            val body = response.body<BaseResponse<NoteDetailData>>()
            Log.d("NoteRepository", "getNoteById success: ${body.data.note.id}")
            Result.success(body.data.note.toDomain())
        } else {
            val error = response.body<ErrorResponse>()
            Log.e("NoteRepository", "getNoteById failed with status: $error")
            Result.failure(Exception("Failed to fetch note by ID: ${error.error}"))
        }
    } catch (e: Exception) {
        Log.e("NoteRepository", "getNoteById exception", e)
        Result.failure(e)
    }

    // Create a new note
    override suspend fun createNote(title: String, content: String?, image: File?): Result<Unit> = try {
        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "api/notes",
            formData = formData {
                // Append text fields to the form-data payload
                append("title", title) // Required text field
                content?.let { append("content", it) } // Optional text field

                // Append image file if available
                image?.let {
                    append(
                        key = "image", // This is the form field name expected by the backend
                        value = it.readBytes(), // File converted to ByteArray
                        headers = Headers.build {
                            // Set the appropriate MIME type for the file based on its extension
                            append(
                                HttpHeaders.ContentType, when {
                                    it.extension.equals("png", true) -> "image/png"
                                    it.extension.equals("gif", true) -> "image/gif"
                                    else -> "image/jpeg" // Default fallback for jpg/jpeg/other
                                }
                            )
                            // Content-Disposition header includes the filename so the server knows what to name the uploaded file
                            append(HttpHeaders.ContentDisposition, "filename=\"${it.name}\"")
                        }
                    )
                }
            }
        )

        if (response.status == HttpStatusCode.Created) {
            Log.d("NoteRepository", "createNote success")
            Result.success(Unit)
        } else {
            val error = response.body<ErrorResponse>()
            Log.e("NoteRepository", "createNote failed with status: $error")
            Result.failure(Exception("Failed to create note ${error.error}"))
        }
    } catch (e: Exception) {
        Log.e("NoteRepository", "createNote exception", e)
        Result.failure(e)
    }

    // Update an existing note by ID, also supports image reupload
    override suspend fun updateNote(
        id: String,
        title: String,
        content: String?,
        image: File?
    ): Result<Unit> = try {
        val response: HttpResponse = client.request("api/notes/$id") {
            method = HttpMethod.Put // Use PUT to update existing note

            // Send multipart/form-data as request body
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("title", title) // Required field
                        content?.let { append("content", it) } // Optional

                        image?.let {
                            append(
                                key = "image", // Backend expects this field name
                                value = it.readBytes(), // Convert file to bytes for upload
                                headers = Headers.build {
                                    // Tell the server the file's MIME type
                                    append(
                                        HttpHeaders.ContentType, when {
                                            it.extension.equals("png", true) -> "image/png"
                                            it.extension.equals("gif", true) -> "image/gif"
                                            else -> "image/jpeg"
                                        }
                                    )
                                    // Provide original filename through Content-Disposition
                                    append(HttpHeaders.ContentDisposition, "filename=\"${it.name}\"")
                                }
                            )
                        }
                    }
                )
            )
        }

        if (response.status == HttpStatusCode.OK) {
            Log.d("NoteRepository", "updateNote success")
            Result.success(Unit)
        } else {
            val error = response.body<ErrorResponse>()
            Log.e("NoteRepository", "updateNote failed with status: $error")
            Result.failure(Exception("Failed to update note : ${error.error}"))
        }
    } catch (e: Exception) {
        Log.e("NoteRepository", "updateNote exception", e)
        Result.failure(e)
    }

    // Delete a note by ID
    override suspend fun deleteNote(id: String): Result<Unit> = try {
        val response = client.delete("api/notes/$id")

        if (response.status == HttpStatusCode.OK) {
            Log.d("NoteRepository", "deleteNote success")
            Result.success(Unit)
        } else {
            val error = response.body<ErrorResponse>()
            Log.e("NoteRepository", "deleteNote failed with status: $error")
            Result.failure(Exception("Failed to delete note: ${error.error}"))
        }
    } catch (e: Exception) {
        Log.e("NoteRepository", "deleteNote exception", e)
        Result.failure(e)
    }
}