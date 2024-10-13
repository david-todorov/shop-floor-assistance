package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.services.database.objects.WorkflowDBO;
import com.shopfloor.backend.services.workflows.EditorService;
import com.shopfloor.backend.services.workflows.EditorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *                            PLEASE READ
 * This is a blueprint for controller which uses EditorServiceImpl
 * In our case we use it through an interface called EditorService
 * Any logic should be in declared in EditorService interface first
 * Then EditorServiceImpl implements it
 * Finally EditorWorkflowController uses it
 *                               IMPORTANT
 * No concrete implementations here, just use the "editorService"
 * This ensures that the implementation is flexible and scalable
 * Thank you for your time, now go implement
 **/
@RestController
@RequestMapping("/workflows/editor")
public class EditorWorkflowController {

    private final EditorService editorService;

    @Autowired
    public EditorWorkflowController(EditorServiceImpl editorService) {
        this.editorService = editorService;
    }

    // POST /workflows/editor
    @PostMapping
    public ResponseEntity<String> createWorkflow() {
        // Logic to create a new workflow
        return ResponseEntity.ok("Workflow created");
    }

    // PUT /workflows/editor/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> updateWorkflow(@PathVariable Long id, @RequestBody WorkflowDBO workflowDBO) {
        // Logic to update an existing workflow
        return ResponseEntity.ok("Workflow updated");
    }

    // DELETE /workflows/editor/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkflow(@PathVariable Long id) {
        // Logic to delete a workflow
        return ResponseEntity.ok("Workflow deleted");
    }

    // Additional methods for handling steps, etc.
}
