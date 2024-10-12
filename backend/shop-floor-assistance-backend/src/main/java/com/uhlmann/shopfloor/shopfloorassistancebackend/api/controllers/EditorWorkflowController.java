package com.uhlmann.shopfloor.shopfloorassistancebackend.api.controllers;

import com.uhlmann.shopfloor.shopfloorassistancebackend.database.objects.WorkflowDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflows/editor")
public class EditorWorkflowController {

    // POST /workflows/editor
    @PostMapping
    public ResponseEntity<String> createWorkflow(@RequestBody WorkflowDTO workflowDTO) {
        // Logic to create a new workflow
        return ResponseEntity.ok("Workflow created");
    }

    // PUT /workflows/editor/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> updateWorkflow(@PathVariable Long id, @RequestBody WorkflowDTO workflowDTO) {
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
