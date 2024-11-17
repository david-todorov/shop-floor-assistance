import { itemTO } from "./itemTO"
import { orderTO } from "./orderTO"
import { taskTO } from "./taskTO"
import { workflowTO } from "./workflowTO"

// Delete after debugging..
export const dummyOrder: orderTO= {
    "orderNumber": "W0005",
    "name": "EEE",
    "description": "Aspirin to Opium",
    "equipment": [
        { "id": 1,
        "equipmentNumber": "E0001",
        "name": "BEC 500_123",
        "description": "High end Uhlmann packaging Machine",
        "type": "Type BEC500" },
        { "id": 2,
        "equipmentNumber": "E0002",
        "name": "Format set abdc",
        "description": "Specific format set for BEC 500",
        "type": "8x2 blister"
         }
    ],
    "productBefore":
        {   "id": 1,
            "productNumber": "P0001",
            "name": "Aspirin XYZ",
            "type": "Medicine",
            "country": "Germany",
            "packageSize": "8x2",
            "packageType": "Blister",
            "language": "German",
            "description": "8x2 Blister"
        },
    "productAfter":
        {   "id": 5,
            "productNumber": "P0002",
            "name": "Opium ABC",
            "type": "Medicine",
            "description": "8x2 Blister",
            "language": "German",
            "country": "Germany",
            "packageSize": "8x2",
            "packageType": "Blister"
        },
    "workflows": [
        {
            "name": "Change Over",
            "description": "Transition machine settings for a new product batch",
            "tasks": [
                {
                    "name": "Cleaning",
                    "description": "Clean machine surfaces to maintain hygiene standards",
                    "items": [
                        {
                            "name": "Remove Tablets",
                            "description": "Safely remove any remaining tablets from the machine",
                            "timeRequired": 25
                        },
                        {
                            "name": "Remove Leaflets",
                            "description": "Clear out any unused leaflets from previous production",
                            "timeRequired": 10
                        },
                        {
                            "name": "Remove Cartons",
                            "description": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Mounting",
                    "description": "Mounting",
                    "items": [
                        {
                            "name": "Check Heating Plates",
                            "description": "Ensure heating plates are properly functioning and aligned",
                            "timeRequired": 10
                        },
                        {
                            "name": "Change Forming Layout",
                            "description": "Adjust the forming layout to match the new product requirements",
                            "timeRequired": 60
                        },
                        {
                            "name": "Change Sealing Roll",
                            "description": "Replace or adjust the sealing roll for the new product",
                            "timeRequired": 45
                        },
                        {
                            "name": "Set manual parameters",
                            "description": "Update machine settings according to product specifications",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Testing",
                    "description": "Run initials tests to confirm functionality",
                    "items": [
                        {
                            "name": "Check manual parameters",
                            "description": "Verify that all manual settings are correct",
                            "timeRequired": 10
                        },
                        {
                            "name": "Dry run in 'mechanical test mode'",
                            "description": "Perform a dry run to check mechanical operations",
                            "timeRequired": 15
                        },
                        {
                            "name": "Check sealing temperature",
                            "description": "The temperature range is between 185°C and 195°C",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Release",
                    "description": "Approve the machine for production after successful checks",
                    "items": [
                        {
                            "name": "Confirm complete Change Over checklist",
                            "description": "Verify that all steps in the checklist have been completed",
                            "timeRequired": 1
                        },
                        {
                            "name": "Digital signature",
                            "description": "Sign off digitally to confirm the changeover process is complete",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        },
        {
            "name": "Commissioning",
            "description": "Prepare the machine for production",
            "tasks": [
                {
                    "name": "Thread in forming film",
                    "description": "Feed the forming film into the machine",
                    "items": [
                        {
                            "name": "Thread forming film to unwinding, tighten clamping",
                            "description": "Guide the forming film to the unwinding section and secure the clamp",
                            "timeRequired": 40
                        },
                        {
                            "name": "Activate 'manual unwinding' on HMI",
                            "description": "Start manual unwinding through the HMI",
                            "timeRequired": 1
                        },
                        {
                            "name": "Thread forming film to filling section",
                            "description": "Direct the forming film to the filling section",
                            "timeRequired": 50
                        },
                        {
                            "name": "Clamp index after forming",
                            "description": "Secure the film at the index after forming",
                            "timeRequired": 5
                        }
                    ]
                },
                {
                    "name": "Thread in lid foil",
                    "description": "Feed the lid foil into the machine",
                    "items": [
                        {
                            "name": "Thread lid foil to sealed index",
                            "description": "Thread the lid foil to the sealed index",
                            "timeRequired": 30
                        },
                        {
                            "name": "Tape lid foil onto forming film",
                            "description": "Attach the lid foil to the forming film with tape",
                            "timeRequired": 2
                        },
                        {
                            "name": "Wind lid foil until pendulum is in working position",
                            "description": "Wind the lid foil until the pendulum is in its working position",
                            "timeRequired": 60
                        }
                    ]
                },
                {
                    "name": "Insert Cartons & Leaflets",
                    "description": "Load the machine with cartons and leaflets",
                    "items": [
                        {
                            "name": "Insert Leaflets",
                            "description": "Insert leaflets into the designated area",
                            "timeRequired": 10
                        },
                        {
                            "name": "Insert Cartons",
                            "description": "Insert cartons into the machine",
                            "timeRequired": 10
                        }
                    ]
                }
            ]
        },
        {
            "name": "Rampup",
            "description": "Gradually increase the machine speed during ramp-up",
            "tasks": [
                {
                    "name": "Preparation",
                    "description": "Ensure all preparatory tasks are complete",
                    "items": [
                        {
                            "name": "Close all doors/hatches",
                            "description": "Close all doors and hatches for safety",
                            "timeRequired": 2
                        },
                        {
                            "name": "Set machine to 'ready to produce'",
                            "description": "Switch the machine to 'ready to produce' mode",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Confirmation",
                    "description": "Confirmation",
                    "items": [
                        {
                            "name": "Confirm production readiness",
                            "description": "Confirm the machine is ready for production",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        }
    ]
}

export const sampleWorkflows: workflowTO[]= [
            {
            "name": "Change Over",
            "description": "Transition machine settings for a new product batch",
            "tasks": [
                {
                    "name": "Cleaning",
                    "description": "Clean machine surfaces to maintain hygiene standards",
                    "items": [
                        {
                            "name": "Remove Tablets",
                            "description": "Safely remove any remaining tablets from the machine",
                            "timeRequired": 25
                        },
                        {
                            "name": "Remove Leaflets",
                            "description": "Clear out any unused leaflets from previous production",
                            "timeRequired": 10
                        },
                        {
                            "name": "Remove Cartons",
                            "description": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Mounting",
                    "description": "Mounting",
                    "items": [
                        {
                            "name": "Check Heating Plates",
                            "description": "Ensure heating plates are properly functioning and aligned",
                            "timeRequired": 10
                        },
                        {
                            "name": "Change Forming Layout",
                            "description": "Adjust the forming layout to match the new product requirements",
                            "timeRequired": 60
                        },
                        {
                            "name": "Change Sealing Roll",
                            "description": "Replace or adjust the sealing roll for the new product",
                            "timeRequired": 45
                        },
                        {
                            "name": "Set manual parameters",
                            "description": "Update machine settings according to product specifications",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Testing",
                    "description": "Run initials tests to confirm functionality",
                    "items": [
                        {
                            "name": "Check manual parameters",
                            "description": "Verify that all manual settings are correct",
                            "timeRequired": 10
                        },
                        {
                            "name": "Dry run in 'mechanical test mode'",
                            "description": "Perform a dry run to check mechanical operations",
                            "timeRequired": 15
                        },
                        {
                            "name": "Check sealing temperature",
                            "description": "The temperature range is between 185°C and 195°C",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Release",
                    "description": "Approve the machine for production after successful checks",
                    "items": [
                        {
                            "name": "Confirm complete Change Over checklist",
                            "description": "Verify that all steps in the checklist have been completed",
                            "timeRequired": 1
                        },
                        {
                            "name": "Digital signature",
                            "description": "Sign off digitally to confirm the changeover process is complete",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        },
        {
            "name": "Commissioning",
            "description": "Prepare the machine for production",
            "tasks": [
                {
                    "name": "Thread in forming film",
                    "description": "Feed the forming film into the machine",
                    "items": [
                        {
                            "name": "Thread forming film to unwinding, tighten clamping",
                            "description": "Guide the forming film to the unwinding section and secure the clamp",
                            "timeRequired": 40
                        },
                        {
                            "name": "Activate 'manual unwinding' on HMI",
                            "description": "Start manual unwinding through the HMI",
                            "timeRequired": 1
                        },
                        {
                            "name": "Thread forming film to filling section",
                            "description": "Direct the forming film to the filling section",
                            "timeRequired": 50
                        },
                        {
                            "name": "Clamp index after forming",
                            "description": "Secure the film at the index after forming",
                            "timeRequired": 5
                        }
                    ]
                },
                {
                    "name": "Thread in lid foil",
                    "description": "Feed the lid foil into the machine",
                    "items": [
                        {
                            "name": "Thread lid foil to sealed index",
                            "description": "Thread the lid foil to the sealed index",
                            "timeRequired": 30
                        },
                        {
                            "name": "Tape lid foil onto forming film",
                            "description": "Attach the lid foil to the forming film with tape",
                            "timeRequired": 2
                        },
                        {
                            "name": "Wind lid foil until pendulum is in working position",
                            "description": "Wind the lid foil until the pendulum is in its working position",
                            "timeRequired": 60
                        }
                    ]
                },
                {
                    "name": "Insert Cartons & Leaflets",
                    "description": "Load the machine with cartons and leaflets",
                    "items": [
                        {
                            "name": "Insert Leaflets",
                            "description": "Insert leaflets into the designated area",
                            "timeRequired": 10
                        },
                        {
                            "name": "Insert Cartons",
                            "description": "Insert cartons into the machine",
                            "timeRequired": 10
                        }
                    ]
                }
            ]
        },
        {
            "name": "Rampup",
            "description": "Gradually increase the machine speed during ramp-up",
            "tasks": [
                {
                    "name": "Preparation",
                    "description": "Ensure all preparatory tasks are complete",
                    "items": [
                        {
                            "name": "Close all doors/hatches",
                            "description": "Close all doors and hatches for safety",
                            "timeRequired": 2
                        },
                        {
                            "name": "Set machine to 'ready to produce'",
                            "description": "Switch the machine to 'ready to produce' mode",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Confirmation",
                    "description": "Confirmation",
                    "items": [
                        {
                            "name": "Confirm production readiness",
                            "description": "Confirm the machine is ready for production",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        }
]

export const sampleTasks: taskTO[]= [
                {
                    "name": "Cleaning",
                    "description": "Clean machine surfaces to maintain hygiene standards",
                    "items": [
                        {
                            "name": "Remove Leaflets",
                            "description": "Clear out any unused leaflets from previous production",
                            "timeRequired": 10
                        },
                        {
                            "name": "Remove Cartons",
                            "description": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Release",
                    "description": "Approve the machine for production after successful checks",
                    "items": [
                        {
                            "name": "Confirm complete Change Over checklist",
                            "description": "Verify that all steps in the checklist have been completed",
                            "timeRequired": 1
                        },
                        {
                            "name": "Digital signature",
                            "description": "Sign off digitally to confirm the changeover process is complete",
                            "timeRequired": 1
                        }
                    ]
                }
]

export const sampleItems: itemTO[]=[
                            {
                            "name": "Close all doors/hatches",
                            "description": "Close all doors and hatches for safety",
                            "timeRequired": 2
                        },
                        {
                            "name": "Set machine to 'ready to produce'",
                            "description": "Switch the machine to 'ready to produce' mode",
                            "timeRequired": 1
                        },
                          {
                            "name": "Check manual parameters",
                            "description": "Verify that all manual settings are correct",
                            "timeRequired": 10
                        },
                        {
                            "name": "Dry run in 'mechanical test mode'",
                            "description": "Perform a dry run to check mechanical operations",
                            "timeRequired": 15
                        },
                        {
                            "name": "Check sealing temperature",
                            "description": "The temperature range is between 185°C and 195°C",
                            "timeRequired": 20
                        }

]

export const sampleOrders: orderTO[]= 
[
    {
    "orderNumber": "W0001",
    "name": "AAA",
    "description": "Aspirin max",
    "equipment": [
        { "id": 1,
        "equipmentNumber": "E0001",
        "name": "BEC 500_123",
        "description": "High end Uhlmann packaging Machine",
        "type": "Type BEC500" },
        { "id": 2,
        "equipmentNumber": "E0002",
        "name": "Format set abdc",
        "description": "Specific format set for BEC 500",
        "type": "8x2 blister"
         }
    ],
    "productBefore":
        {   "id": 1,
            "productNumber": "P0001",
            "name": "Aspirin XYZ",
            "type": "Medicine",
            "country": "Germany",
            "packageSize": "8x2",
            "packageType": "Blister",
            "language": "German",
            "description": "8x2 Blister"
        },
    "productAfter":
        {   "id": 4,
            "productNumber": "P0004",
            "name": "Aspirin XYZ",
            "type": "Medicine",
            "description": "8x2 Blister",
            "language": "Spanish",
            "country": "Spain",
            "packageSize": "8x2",
            "packageType": "Blister"
        },
    "workflows": [
        {
            "name": "Change Over",
            "description": "Transition machine settings for a new product batch",
            "tasks": [
                {
                    "name": "Cleaning",
                    "description": "Clean machine surfaces to maintain hygiene standards",
                    "items": [
                        {
                            "name": "Remove Tablets",
                            "description": "Safely remove any remaining tablets from the machine",
                            "timeRequired": 25
                        },
                        {
                            "name": "Remove Leaflets",
                            "description": "Clear out any unused leaflets from previous production",
                            "timeRequired": 10
                        },
                        {
                            "name": "Remove Cartons",
                            "description": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Mounting",
                    "description": "Mounting",
                    "items": [
                        {
                            "name": "Check Heating Plates",
                            "description": "Ensure heating plates are properly functioning and aligned",
                            "timeRequired": 10
                        },
                        {
                            "name": "Change Forming Layout",
                            "description": "Adjust the forming layout to match the new product requirements",
                            "timeRequired": 60
                        },
                        {
                            "name": "Change Sealing Roll",
                            "description": "Replace or adjust the sealing roll for the new product",
                            "timeRequired": 45
                        },
                        {
                            "name": "Set manual parameters",
                            "description": "Update machine settings according to product specifications",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Testing",
                    "description": "Run initials tests to confirm functionality",
                    "items": [
                        {
                            "name": "Check manual parameters",
                            "description": "Verify that all manual settings are correct",
                            "timeRequired": 10
                        },
                        {
                            "name": "Dry run in 'mechanical test mode'",
                            "description": "Perform a dry run to check mechanical operations",
                            "timeRequired": 15
                        },
                        {
                            "name": "Check sealing temperature",
                            "description": "The temperature range is between 185°C and 195°C",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Release",
                    "description": "Approve the machine for production after successful checks",
                    "items": [
                        {
                            "name": "Confirm complete Change Over checklist",
                            "description": "Verify that all steps in the checklist have been completed",
                            "timeRequired": 1
                        },
                        {
                            "name": "Digital signature",
                            "description": "Sign off digitally to confirm the changeover process is complete",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        },
        {
            "name": "Commissioning",
            "description": "Prepare the machine for production",
            "tasks": [
                {
                    "name": "Thread in forming film",
                    "description": "Feed the forming film into the machine",
                    "items": [
                        {
                            "name": "Thread forming film to unwinding, tighten clamping",
                            "description": "Guide the forming film to the unwinding section and secure the clamp",
                            "timeRequired": 40
                        },
                        {
                            "name": "Activate 'manual unwinding' on HMI",
                            "description": "Start manual unwinding through the HMI",
                            "timeRequired": 1
                        },
                        {
                            "name": "Thread forming film to filling section",
                            "description": "Direct the forming film to the filling section",
                            "timeRequired": 50
                        },
                        {
                            "name": "Clamp index after forming",
                            "description": "Secure the film at the index after forming",
                            "timeRequired": 5
                        }
                    ]
                },
                {
                    "name": "Thread in lid foil",
                    "description": "Feed the lid foil into the machine",
                    "items": [
                        {
                            "name": "Thread lid foil to sealed index",
                            "description": "Thread the lid foil to the sealed index",
                            "timeRequired": 30
                        },
                        {
                            "name": "Tape lid foil onto forming film",
                            "description": "Attach the lid foil to the forming film with tape",
                            "timeRequired": 2
                        },
                        {
                            "name": "Wind lid foil until pendulum is in working position",
                            "description": "Wind the lid foil until the pendulum is in its working position",
                            "timeRequired": 60
                        }
                    ]
                },
                {
                    "name": "Insert Cartons & Leaflets",
                    "description": "Load the machine with cartons and leaflets",
                    "items": [
                        {
                            "name": "Insert Leaflets",
                            "description": "Insert leaflets into the designated area",
                            "timeRequired": 10
                        },
                        {
                            "name": "Insert Cartons",
                            "description": "Insert cartons into the machine",
                            "timeRequired": 10
                        }
                    ]
                }
            ]
        },
        {
            "name": "Rampup",
            "description": "Gradually increase the machine speed during ramp-up",
            "tasks": [
                {
                    "name": "Preparation",
                    "description": "Ensure all preparatory tasks are complete",
                    "items": [
                        {
                            "name": "Close all doors/hatches",
                            "description": "Close all doors and hatches for safety",
                            "timeRequired": 2
                        },
                        {
                            "name": "Set machine to 'ready to produce'",
                            "description": "Switch the machine to 'ready to produce' mode",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Confirmation",
                    "description": "Confirmation",
                    "items": [
                        {
                            "name": "Confirm production readiness",
                            "description": "Confirm the machine is ready for production",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        }
    ]
}
,
    {
    "orderNumber": "W0002",
    "name": "BBB",
    "description": "Aspirin 'language only'",
    "equipment": [
        { "id": 1,
        "equipmentNumber": "E0001",
        "name": "BEC 500_123",
        "description": "High end Uhlmann packaging Machine",
        "type": "Type BEC500" },
        { "id": 2,
        "equipmentNumber": "E0002",
        "name": "Format set abdc",
        "description": "Specific format set for BEC 500",
        "type": "8x2 blister"
         }
    ],
    "productBefore":
        {   "id": 1,
            "productNumber": "P0001",
            "name": "Aspirin XYZ",
            "type": "Medicine",
            "country": "Germany",
            "packageSize": "8x2",
            "packageType": "Blister",
            "language": "German",
            "description": "8x2 Blister"
        },
    "productAfter":
        {   "id": 4,
            "productNumber": "P0004",
            "name": "Aspirin XYZ",
            "type": "Medicine",
            "description": "8x2 Blister",
            "language": "Spanish",
            "country": "Spain",
            "packageSize": "8x2",
            "packageType": "Blister"
        },
    "workflows": [
        {
            "name": "Change Over",
            "description": "Transition machine settings for a new product batch",
            "tasks": [
                {
                    "name": "Cleaning",
                    "description": "Clean machine surfaces to maintain hygiene standards",
                    "items": [
                        {
                            "name": "Remove Leaflets",
                            "description": "Clear out any unused leaflets from previous production",
                            "timeRequired": 10
                        },
                        {
                            "name": "Remove Cartons",
                            "description": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Release",
                    "description": "Approve the machine for production after successful checks",
                    "items": [
                        {
                            "name": "Confirm complete Change Over checklist",
                            "description": "Verify that all steps in the checklist have been completed",
                            "timeRequired": 1
                        },
                        {
                            "name": "Digital signature",
                            "description": "Sign off digitally to confirm the changeover process is complete",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        },
        {
            "name": "Commissioning",
            "description": "Prepare the machine for production",
            "tasks": [
                {
                    "name": "Insert Cartons & Leaflets",
                    "description": "Load the machine with cartons and leaflets",
                    "items": [
                        {
                            "name": "Insert Leaflets",
                            "description": "Insert leaflets into the designated area",
                            "timeRequired": 10
                        },
                        {
                            "name": "Insert Cartons",
                            "description": "Insert cartons into the machine",
                            "timeRequired": 10
                        }
                    ]
                }
            ]
        },
        {
            "name": "Rampup",
            "description": "Gradually increase the machine speed during ramp-up",
            "tasks": [
                {
                    "name": "Preparation",
                    "description": "Ensure all preparatory tasks are complete",
                    "items": [
                        {
                            "name": "Close all doors/hatches",
                            "description": "Close all doors and hatches for safety",
                            "timeRequired": 2
                        },
                        {
                            "name": "Set machine to 'ready to produce'",
                            "description": "Switch the machine to 'ready to produce' mode",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Confirmation",
                    "description": "Confirmation",
                    "items": [
                        {
                            "name": "Confirm production readiness",
                            "description": "Confirm the machine is ready for production",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        }
    ]
},
   {
    "orderNumber": "W0004",
    "name": "DDD",
    "description": "Opium to Aspirin",
    "equipment": [
        { "id": 1,
        "equipmentNumber": "E0001",
        "name": "BEC 500_123",
        "description": "High end Uhlmann packaging Machine",
        "type": "Type BEC500" },
        { "id": 2,
        "equipmentNumber": "E0002",
        "name": "Format set abdc",
        "description": "Specific format set for BEC 500",
        "type": "8x2 blister"
         }
    ],
    "productBefore":
        {   "id": 5,
            "productNumber": "P0002",
            "name": "Opium ABC",
            "type": "Medicine",
            "description": "8x2 Blister",
            "language": "German",
            "country": "Germany",
            "packageSize": "8x2",
            "packageType": "Blister"
        },
    "productAfter":
        {   "id": 1,
            "productNumber": "P0001",
            "name": "Aspirin XYZ",
            "type": "Medicine",
            "country": "Germany",
            "packageSize": "8x2",
            "packageType": "Blister",
            "language": "German",
            "description": "8x2 Blister"
        },
    "workflows": [
        {
            "name": "Change Over",
            "description": "Transition machine settings for a new product batch",
            "tasks": [
                {
                    "name": "Cleaning",
                    "description": "Clean machine surfaces to maintain hygiene standards",
                    "items": [
                        {
                            "name": "Remove Tablets",
                            "description": "Safely remove any remaining tablets from the machine",
                            "timeRequired": 25
                        },
                        {
                            "name": "Remove Leaflets",
                            "description": "Clear out any unused leaflets from previous production",
                            "timeRequired": 10
                        },
                        {
                            "name": "Remove Cartons",
                            "description": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Mounting",
                    "description": "Mounting",
                    "items": [
                        {
                            "name": "Check Heating Plates",
                            "description": "Ensure heating plates are properly functioning and aligned",
                            "timeRequired": 10
                        },
                        {
                            "name": "Change Forming Layout",
                            "description": "Adjust the forming layout to match the new product requirements",
                            "timeRequired": 60
                        },
                        {
                            "name": "Change Sealing Roll",
                            "description": "Replace or adjust the sealing roll for the new product",
                            "timeRequired": 45
                        },
                        {
                            "name": "Set manual parameters",
                            "description": "Update machine settings according to product specifications",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Testing",
                    "description": "Run initials tests to confirm functionality",
                    "items": [
                        {
                            "name": "Check manual parameters",
                            "description": "Verify that all manual settings are correct",
                            "timeRequired": 10
                        },
                        {
                            "name": "Dry run in 'mechanical test mode'",
                            "description": "Perform a dry run to check mechanical operations",
                            "timeRequired": 15
                        },
                        {
                            "name": "Check sealing temperature",
                            "description": "The temperature range is between 185°C and 195°C",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Release",
                    "description": "Approve the machine for production after successful checks",
                    "items": [
                        {
                            "name": "Confirm complete Change Over checklist",
                            "description": "Verify that all steps in the checklist have been completed",
                            "timeRequired": 1
                        },
                        {
                            "name": "Digital signature",
                            "description": "Sign off digitally to confirm the changeover process is complete",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        },
        {
            "name": "Commissioning",
            "description": "Prepare the machine for production",
            "tasks": [
                {
                    "name": "Thread in forming film",
                    "description": "Feed the forming film into the machine",
                    "items": [
                        {
                            "name": "Thread forming film to unwinding, tighten clamping",
                            "description": "Guide the forming film to the unwinding section and secure the clamp",
                            "timeRequired": 40
                        },
                        {
                            "name": "Activate 'manual unwinding' on HMI",
                            "description": "Start manual unwinding through the HMI",
                            "timeRequired": 1
                        },
                        {
                            "name": "Thread forming film to filling section",
                            "description": "Direct the forming film to the filling section",
                            "timeRequired": 50
                        },
                        {
                            "name": "Clamp index after forming",
                            "description": "Secure the film at the index after forming",
                            "timeRequired": 5
                        }
                    ]
                },
                {
                    "name": "Thread in lid foil",
                    "description": "Feed the lid foil into the machine",
                    "items": [
                        {
                            "name": "Thread lid foil to sealed index",
                            "description": "Thread the lid foil to the sealed index",
                            "timeRequired": 30
                        },
                        {
                            "name": "Tape lid foil onto forming film",
                            "description": "Attach the lid foil to the forming film with tape",
                            "timeRequired": 2
                        },
                        {
                            "name": "Wind lid foil until pendulum is in working position",
                            "description": "Wind the lid foil until the pendulum is in its working position",
                            "timeRequired": 60
                        }
                    ]
                },
                {
                    "name": "Insert Cartons & Leaflets",
                    "description": "Load the machine with cartons and leaflets",
                    "items": [
                        {
                            "name": "Insert Leaflets",
                            "description": "Insert leaflets into the designated area",
                            "timeRequired": 10
                        },
                        {
                            "name": "Insert Cartons",
                            "description": "Insert cartons into the machine",
                            "timeRequired": 10
                        }
                    ]
                }
            ]
        },
        {
            "name": "Rampup",
            "description": "Gradually increase the machine speed during ramp-up",
            "tasks": [
                {
                    "name": "Preparation",
                    "description": "Ensure all preparatory tasks are complete",
                    "items": [
                        {
                            "name": "Close all doors/hatches",
                            "description": "Close all doors and hatches for safety",
                            "timeRequired": 2
                        },
                        {
                            "name": "Set machine to 'ready to produce'",
                            "description": "Switch the machine to 'ready to produce' mode",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Confirmation",
                    "description": "Confirmation",
                    "items": [
                        {
                            "name": "Confirm production readiness",
                            "description": "Confirm the machine is ready for production",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        }
    ]
}
,
   {
    "orderNumber": "W0005",
    "name": "EEE",
    "description": "Aspirin to Opium",
    "equipment": [
        { "id": 1,
        "equipmentNumber": "E0001",
        "name": "BEC 500_123",
        "description": "High end Uhlmann packaging Machine",
        "type": "Type BEC500" },
        { "id": 2,
        "equipmentNumber": "E0002",
        "name": "Format set abdc",
        "description": "Specific format set for BEC 500",
        "type": "8x2 blister"
         }
    ],
    "productBefore":
        {   "id": 1,
            "productNumber": "P0001",
            "name": "Aspirin XYZ",
            "type": "Medicine",
            "country": "Germany",
            "packageSize": "8x2",
            "packageType": "Blister",
            "language": "German",
            "description": "8x2 Blister"
        },
    "productAfter":
        {   "id": 5,
            "productNumber": "P0002",
            "name": "Opium ABC",
            "type": "Medicine",
            "description": "8x2 Blister",
            "language": "German",
            "country": "Germany",
            "packageSize": "8x2",
            "packageType": "Blister"
        },
    "workflows": [
        {
            "name": "Change Over",
            "description": "Transition machine settings for a new product batch",
            "tasks": [
                {
                    "name": "Cleaning",
                    "description": "Clean machine surfaces to maintain hygiene standards",
                    "items": [
                        {
                            "name": "Remove Tablets",
                            "description": "Safely remove any remaining tablets from the machine",
                            "timeRequired": 25
                        },
                        {
                            "name": "Remove Leaflets",
                            "description": "Clear out any unused leaflets from previous production",
                            "timeRequired": 10
                        },
                        {
                            "name": "Remove Cartons",
                            "description": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Mounting",
                    "description": "Mounting",
                    "items": [
                        {
                            "name": "Check Heating Plates",
                            "description": "Ensure heating plates are properly functioning and aligned",
                            "timeRequired": 10
                        },
                        {
                            "name": "Change Forming Layout",
                            "description": "Adjust the forming layout to match the new product requirements",
                            "timeRequired": 60
                        },
                        {
                            "name": "Change Sealing Roll",
                            "description": "Replace or adjust the sealing roll for the new product",
                            "timeRequired": 45
                        },
                        {
                            "name": "Set manual parameters",
                            "description": "Update machine settings according to product specifications",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Testing",
                    "description": "Run initials tests to confirm functionality",
                    "items": [
                        {
                            "name": "Check manual parameters",
                            "description": "Verify that all manual settings are correct",
                            "timeRequired": 10
                        },
                        {
                            "name": "Dry run in 'mechanical test mode'",
                            "description": "Perform a dry run to check mechanical operations",
                            "timeRequired": 15
                        },
                        {
                            "name": "Check sealing temperature",
                            "description": "The temperature range is between 185°C and 195°C",
                            "timeRequired": 20
                        }
                    ]
                },
                {
                    "name": "Release",
                    "description": "Approve the machine for production after successful checks",
                    "items": [
                        {
                            "name": "Confirm complete Change Over checklist",
                            "description": "Verify that all steps in the checklist have been completed",
                            "timeRequired": 1
                        },
                        {
                            "name": "Digital signature",
                            "description": "Sign off digitally to confirm the changeover process is complete",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        },
        {
            "name": "Commissioning",
            "description": "Prepare the machine for production",
            "tasks": [
                {
                    "name": "Thread in forming film",
                    "description": "Feed the forming film into the machine",
                    "items": [
                        {
                            "name": "Thread forming film to unwinding, tighten clamping",
                            "description": "Guide the forming film to the unwinding section and secure the clamp",
                            "timeRequired": 40
                        },
                        {
                            "name": "Activate 'manual unwinding' on HMI",
                            "description": "Start manual unwinding through the HMI",
                            "timeRequired": 1
                        },
                        {
                            "name": "Thread forming film to filling section",
                            "description": "Direct the forming film to the filling section",
                            "timeRequired": 50
                        },
                        {
                            "name": "Clamp index after forming",
                            "description": "Secure the film at the index after forming",
                            "timeRequired": 5
                        }
                    ]
                },
                {
                    "name": "Thread in lid foil",
                    "description": "Feed the lid foil into the machine",
                    "items": [
                        {
                            "name": "Thread lid foil to sealed index",
                            "description": "Thread the lid foil to the sealed index",
                            "timeRequired": 30
                        },
                        {
                            "name": "Tape lid foil onto forming film",
                            "description": "Attach the lid foil to the forming film with tape",
                            "timeRequired": 2
                        },
                        {
                            "name": "Wind lid foil until pendulum is in working position",
                            "description": "Wind the lid foil until the pendulum is in its working position",
                            "timeRequired": 60
                        }
                    ]
                },
                {
                    "name": "Insert Cartons & Leaflets",
                    "description": "Load the machine with cartons and leaflets",
                    "items": [
                        {
                            "name": "Insert Leaflets",
                            "description": "Insert leaflets into the designated area",
                            "timeRequired": 10
                        },
                        {
                            "name": "Insert Cartons",
                            "description": "Insert cartons into the machine",
                            "timeRequired": 10
                        }
                    ]
                }
            ]
        },
        {
            "name": "Rampup",
            "description": "Gradually increase the machine speed during ramp-up",
            "tasks": [
                {
                    "name": "Preparation",
                    "description": "Ensure all preparatory tasks are complete",
                    "items": [
                        {
                            "name": "Close all doors/hatches",
                            "description": "Close all doors and hatches for safety",
                            "timeRequired": 2
                        },
                        {
                            "name": "Set machine to 'ready to produce'",
                            "description": "Switch the machine to 'ready to produce' mode",
                            "timeRequired": 1
                        }
                    ]
                },
                {
                    "name": "Confirmation",
                    "description": "Confirmation",
                    "items": [
                        {
                            "name": "Confirm production readiness",
                            "description": "Confirm the machine is ready for production",
                            "timeRequired": 1
                        }
                    ]
                }
            ]
        }
    ]
},
]              
