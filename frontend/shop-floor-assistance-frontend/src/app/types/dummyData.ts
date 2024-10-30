import { orderTO } from "./orderTO"

// Delete after debugging..
export const dummyOrder: orderTO= {
                orderNumber: "W0001",
                name: "AAA",
                shortDescription: "Aspirin max",
                workflows: [
                    {
                        name: "Change Over",
                        description: "Transition machine settings for a new product batch",
                        tasks: [
                            {
                                name: "Cleaning",
                                description: "Clean machine surfaces to maintain hygiene standards",
                                items: [
                                    {
                                        name: "Remove Tablets",
                                        longDescription: "Safely remove any remaining tablets from the machine",
                                        timeRequired: 25
                                    },
                                    {
                                        name: "Remove Leaflets",
                                        longDescription: "Clear out any unused leaflets from previous production",
                                        timeRequired: 10
                                    },
                                    {
                                        name: "Remove Cartons",
                                        longDescription: "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
                                        timeRequired: 20
                                    }
                                ]
                            },
                            {
                                name: "Mounting",
                                description: "Mounting",
                                items: [
                                    {
                                        name: "Check Heating Plates",
                                        longDescription: "Ensure heating plates are properly functioning and aligned",
                                        timeRequired: 10
                                    },
                                    {
                                        name: "Change Forming Layout",
                                        longDescription: "Adjust the forming layout to match the new product requirements",
                                        timeRequired: 60
                                    },
                                    {
                                        name: "Change Sealing Roll",
                                        longDescription: "Replace or adjust the sealing roll for the new product",
                                        timeRequired: 45
                                    },
                                    {
                                        name: "Set manual parameters",
                                        longDescription: "Update machine settings according to product specifications",
                                        timeRequired: null
                                    }
                                ]
                            },
                            {
                                name: "Testing",
                                description: "Run initials tests to confirm functionality",
                                items: [
                                    {
                                        name: "Check manual parameters",
                                        longDescription: "Verify that all manual settings are correct",
                                        timeRequired: 10
                                    },
                                    {
                                        name: "Dry run in 'mechanical test mode'",
                                        longDescription: "Perform a dry run to check mechanical operations",
                                        timeRequired: 15
                                    },
                                    {
                                        name: "Check sealing temperature",
                                        longDescription: "The temperature Ragne is between 185°C and 195°C",
                                        timeRequired: 20
                                    }
                                ]
                            },
                            {
                                name: "Release",
                                description: "Approve the machine for production after successful checks",
                                items: [
                                    {
                                        name: "Confirm complete Change Over checklist",
                                        longDescription: "Verify that all steps in the checklist have been completed",
                                        timeRequired: 1
                                    },
                                    {
                                        name: "Digital signature",
                                        longDescription: "Sign off digitally to confirm the changeover process is complete",
                                        timeRequired: 1
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        name: "Commissioning",
                        description: "Prepare the machine for production",
                        tasks: [
                            {
                                name: "Thread in forming film",
                                description: "Feed the forming film into the machine",
                                items: [
                                    {
                                        name: "Thread forming film to unwinding, tighten clamping",
                                        longDescription: "Guide the forming film to the unwinding section and secure the clamp",
                                        timeRequired: 40
                                    },
                                    {
                                        name: "Activate 'manual unwinding' on HMI",
                                        longDescription: "Start manual unwinding through the HMI",
                                        timeRequired: 1
                                    },
                                    {
                                        name: "Thread forming film to filling section",
                                        longDescription: "Direct the forming film to the filling section",
                                        timeRequired: 50
                                    },
                                    {
                                        name: "Clamp index after forming",
                                        longDescription: "Secure the film at the index after forming",
                                        timeRequired: 5
                                    }
                                ]
                            },
                            {
                                name: "Thread in lid foil",
                                description: "Feed the lid foil into the machine",
                                items: [
                                    {
                                        name: "Thread lid foil to sealed index",
                                        longDescription: "Thread the lid foil to the sealed index",
                                        timeRequired: 30
                                    },
                                    {
                                        name: "Tape lid foil onto forming film",
                                        longDescription: "Attach the lid foil to the forming lid with tape",
                                        timeRequired: 2
                                    },
                                    {
                                        name: "Wind lid foil until pendulum is in working position",
                                        longDescription: "Wind the lid foil until the pendulum is in its working position",
                                        timeRequired: 60
                                    }
                                ]
                            },
                            {
                                name: "Insert Cartons & Leaflets",
                                description: "Load the machine with cartons and leaflets",
                                items: [
                                    {
                                        name: "Insert Leaflets",
                                        longDescription: "Insert leaflets into the designated area",
                                        timeRequired: 10
                                    },
                                    {

                                       name: "Insert Cartons",
                                        longDescription: "Insert cartons into the machine ",
                                        timeRequired: 10
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        name: "Rampup",
                        description: "Gradually increase the machine speed during ramp-up",
                        tasks: [
                            {
                                name: "Preparation",
                                description: "Ensure all preparatory tasks are complete",
                                items: [
                                    {
                                        name: "Close all doors/hatches",
                                        longDescription: "Close all doors and hatches for safety",
                                        timeRequired: 2
                                    },
                                    {
                                        name: "Set machine to 'ready to produce'",
                                        longDescription: "Switch the machine to 'ready to produce' mode",
                                        timeRequired: 1
                                    }
                                ]
                            },
                            {
                                name: "Confirmation",
                                description: "Confirmation",
                                items: [
                                    {
                                        name: "Confirm production readiness",
                                        longDescription: "Confirm the machine is ready for production",
                                        timeRequired: 1
                                    }
                                ]
                            }
                        ]

                    }
                ]
            }

export const sampleOrders: orderTO[]= [
    {
        "orderNumber": "W00011",
        "name": "AAA",
        "shortDescription": "Aspirin max",
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
                                "longDescription": "Safely remove any remaining tablets from the machine",
                                "timeRequired": 25
                            },
                            {
                                "name": "Remove Leaflets",
                                "longDescription": "Clear out any unused leaflets from previous production",
                                "timeRequired": 10
                            },
                            {
                                "name": "Remove Cartons",
                                "longDescription": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
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
                                "longDescription": "Ensure heating plates are properly functioning and aligned",
                                "timeRequired": 10
                            },
                            {
                                "name": "Change Forming Layout",
                                "longDescription": "Adjust the forming layout to match the new product requirements",
                                "timeRequired": 60
                            },
                            {
                                "name": "Change Sealing Roll",
                                "longDescription": "Replace or adjust the sealing roll for the new product",
                                "timeRequired": 45
                            },
                            {
                                "name": "Set manual parameters",
                                "longDescription": "Update machine settings according to product specifications",
                                "timeRequired": null
                            }
                        ]
                    },
                    {
                        "name": "Testing",
                        "description": "Run initials tests to confirm functionality",
                        "items": [
                            {
                                "name": "Check manual parameters",
                                "longDescription": "Verify that all manual settings are correct",
                                "timeRequired": 10
                            },
                            {
                                "name": "Dry run in 'mechanical test mode'",
                                "longDescription": "Perform a dry run to check mechanical operations",
                                "timeRequired": 15
                            },
                            {
                                "name": "Check sealing temperature",
                                "longDescription": "The temperature Ragne is between 185°C and 195°C",
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
                                "longDescription": "Verify that all steps in the checklist have been completed",
                                "timeRequired": 1
                            },
                            {
                                "name": "Digital signature",
                                "longDescription": "Sign off digitally to confirm the changeover process is complete",
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
                                "longDescription": "Guide the forming film to the unwinding section and secure the clamp",
                                "timeRequired": 40
                            },
                            {
                                "name": "Activate 'manual unwinding' on HMI",
                                "longDescription": "Start manual unwinding through the HMI",
                                "timeRequired": 1
                            },
                            {
                                "name": "Thread forming film to filling section",
                                "longDescription": "Direct the forming film to the filling section",
                                "timeRequired": 50
                            },
                            {
                                "name": "Clamp index after forming",
                                "longDescription": "Secure the film at the index after forming",
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
                                "longDescription": "Thread the lid foil to the sealed index",
                                "timeRequired": 30
                            },
                            {
                                "name": "Tape lid foil onto forming film",
                                "longDescription": "Attach the lid foil to the forming lid with tape",
                                "timeRequired": 2
                            },
                            {
                                "name": "Wind lid foil until pendulum is in working position",
                                "longDescription": "Wind the lid foil until the pendulum is in its working position",
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
                                "longDescription": "Insert leaflets into the designated area",
                                "timeRequired": 10
                            },
                            {
                                "name": "Insert Cartons",
                                "longDescription": "Insert cartons into the machine ",
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
                                "longDescription": "Close all doors and hatches for safety",
                                "timeRequired": 2
                            },
                            {
                                "name": "Set machine to 'ready to produce'",
                                "longDescription": "Switch the machine to 'ready to produce' mode",
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
                                "longDescription": "Confirm the machine is ready for production",
                                "timeRequired": 1
                            }
                        ]
                    }
                ]
            }
        ]
    },
    {
        "orderNumber": "W0002",
        "name": "BBB",
        "shortDescription": "Aspirin 'language only'",
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
                                "longDescription": "Clear out any unused leaflets from previous production",
                                "timeRequired": 10
                            },
                            {
                                "name": "Remove Cartons",
                                "longDescription": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
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
                                "longDescription": "Verify that all steps in the checklist have been completed",
                                "timeRequired": 1
                            },
                            {
                                "name": "Digital signature",
                                "longDescription": "Sign off digitally to confirm the changeover process is complete",
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
                                "longDescription": "Insert leaflets into the designated area",
                                "timeRequired": 10
                            },
                            {
                                "name": "Insert Cartons",
                                "longDescription": "Insert cartons into the machine ",
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
                                "longDescription": "Close all doors and hatches for safety",
                                "timeRequired": 2
                            },
                            {
                                "name": "Set machine to 'ready to produce'",
                                "longDescription": "Switch the machine to 'ready to produce' mode",
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
                                "longDescription": "Confirm the machine is ready for production",
                                "timeRequired": 1
                            }
                        ]
                    }
                ]
            }
        ]
    },
    {
        "orderNumber": "W0003",
        "name": "CCC",
        "shortDescription": "Aspirin to larger blister",
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
                                "longDescription": "Clear out any unused leaflets from previous production",
                                "timeRequired": 10
                            },
                            {
                                "name": "Remove Cartons",
                                "longDescription": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
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
                                "longDescription": "Ensure heating plates are properly functioning and aligned",
                                "timeRequired": 10
                            },
                            {
                                "name": "Change Forming Layout",
                                "longDescription": "Adjust the forming layout to match the new product requirements",
                                "timeRequired": 60
                            },
                            {
                                "name": "Change Sealing Roll",
                                "longDescription": "Replace or adjust the sealing roll for the new product",
                                "timeRequired": 45
                            },
                            {
                                "name": "Set manual parameters",
                                "longDescription": "Update machine settings according to product specifications",
                                "timeRequired": null
                            }
                        ]
                    },
                    {
                        "name": "Testing",
                        "description": "Run initials tests to confirm functionality",
                        "items": [
                            {
                                "name": "Check manual parameters",
                                "longDescription": "Verify that all manual settings are correct",
                                "timeRequired": 10
                            },
                            {
                                "name": "Dry run in 'mechanical test mode'",
                                "longDescription": "Perform a dry run to check mechanical operations",
                                "timeRequired": 15
                            },
                            {
                                "name": "Check sealing temperature",
                                "longDescription": "The temperature Ragne is between 185°C and 195°C",
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
                                "longDescription": "Verify that all steps in the checklist have been completed",
                                "timeRequired": 1
                            },
                            {
                                "name": "Digital signature",
                                "longDescription": "Sign off digitally to confirm the changeover process is complete",
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
                                "longDescription": "Guide the forming film to the unwinding section and secure the clamp",
                                "timeRequired": 40
                            },
                            {
                                "name": "Activate 'manual unwinding' on HMI",
                                "longDescription": "Start manual unwinding through the HMI",
                                "timeRequired": 1
                            },
                            {
                                "name": "Thread forming film to filling section",
                                "longDescription": "Direct the forming film to the filling section",
                                "timeRequired": 50
                            },
                            {
                                "name": "Clamp index after forming",
                                "longDescription": "Secure the film at the index after forming",
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
                                "longDescription": "Thread the lid foil to the sealed index",
                                "timeRequired": 30
                            },
                            {
                                "name": "Tape lid foil onto forming film",
                                "longDescription": "Attach the lid foil to the forming lid with tape",
                                "timeRequired": 2
                            },
                            {
                                "name": "Wind lid foil until pendulum is in working position",
                                "longDescription": "Wind the lid foil until the pendulum is in its working position",
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
                                "longDescription": "Insert leaflets into the designated area",
                                "timeRequired": 10
                            },
                            {
                                "name": "Insert Cartons",
                                "longDescription": "Insert cartons into the machine ",
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
                                "longDescription": "Close all doors and hatches for safety",
                                "timeRequired": 2
                            },
                            {
                                "name": "Set machine to 'ready to produce'",
                                "longDescription": "Switch the machine to 'ready to produce' mode",
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
                                "longDescription": "Confirm the machine is ready for production",
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
        "shortDescription": "Opium to Aspirin",
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
                                "longDescription": "Safely remove any remaining tablets from the machine",
                                "timeRequired": 25
                            },
                            {
                                "name": "Remove Leaflets",
                                "longDescription": "Clear out any unused leaflets from previous production",
                                "timeRequired": 10
                            },
                            {
                                "name": "Remove Cartons",
                                "longDescription": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
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
                                "longDescription": "Ensure heating plates are properly functioning and aligned",
                                "timeRequired": 10
                            },
                            {
                                "name": "Change Forming Layout",
                                "longDescription": "Adjust the forming layout to match the new product requirements",
                                "timeRequired": 60
                            },
                            {
                                "name": "Change Sealing Roll",
                                "longDescription": "Replace or adjust the sealing roll for the new product",
                                "timeRequired": 45
                            },
                            {
                                "name": "Set manual parameters",
                                "longDescription": "Update machine settings according to product specifications",
                                "timeRequired": null
                            }
                        ]
                    },
                    {
                        "name": "Testing",
                        "description": "Run initials tests to confirm functionality",
                        "items": [
                            {
                                "name": "Check manual parameters",
                                "longDescription": "Verify that all manual settings are correct",
                                "timeRequired": 10
                            },
                            {
                                "name": "Dry run in 'mechanical test mode'",
                                "longDescription": "Perform a dry run to check mechanical operations",
                                "timeRequired": 15
                            },
                            {
                                "name": "Check sealing temperature",
                                "longDescription": "The temperature Ragne is between 185°C and 195°C",
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
                                "longDescription": "Verify that all steps in the checklist have been completed",
                                "timeRequired": 1
                            },
                            {
                                "name": "Digital signature",
                                "longDescription": "Sign off digitally to confirm the changeover process is complete",
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
                                "longDescription": "Guide the forming film to the unwinding section and secure the clamp",
                                "timeRequired": 40
                            },
                            {
                                "name": "Activate 'manual unwinding' on HMI",
                                "longDescription": "Start manual unwinding through the HMI",
                                "timeRequired": 1
                            },
                            {
                                "name": "Thread forming film to filling section",
                                "longDescription": "Direct the forming film to the filling section",
                                "timeRequired": 50
                            },
                            {
                                "name": "Clamp index after forming",
                                "longDescription": "Secure the film at the index after forming",
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
                                "longDescription": "Thread the lid foil to the sealed index",
                                "timeRequired": 30
                            },
                            {
                                "name": "Tape lid foil onto forming film",
                                "longDescription": "Attach the lid foil to the forming lid with tape",
                                "timeRequired": 2
                            },
                            {
                                "name": "Wind lid foil until pendulum is in working position",
                                "longDescription": "Wind the lid foil until the pendulum is in its working position",
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
                                "longDescription": "Insert leaflets into the designated area",
                                "timeRequired": 10
                            },
                            {
                                "name": "Insert Cartons",
                                "longDescription": "Insert cartons into the machine ",
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
                                "longDescription": "Close all doors and hatches for safety",
                                "timeRequired": 2
                            },
                            {
                                "name": "Set machine to 'ready to produce'",
                                "longDescription": "Switch the machine to 'ready to produce' mode",
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
                                "longDescription": "Confirm the machine is ready for production",
                                "timeRequired": 1
                            }
                        ]
                    }
                ]
            }
        ]
    },
    {
        "orderNumber": "W0005",
        "name": "EEE",
        "shortDescription": "Aspirin to Opium",
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
                                "longDescription": "Safely remove any remaining tablets from the machine",
                                "timeRequired": 25
                            },
                            {
                                "name": "Remove Leaflets",
                                "longDescription": "Clear out any unused leaflets from previous production",
                                "timeRequired": 10
                            },
                            {
                                "name": "Remove Cartons",
                                "longDescription": "Remove any leftover cartons from the machine to prevent mix-ups with the new batch",
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
                                "longDescription": "Ensure heating plates are properly functioning and aligned",
                                "timeRequired": 10
                            },
                            {
                                "name": "Change Forming Layout",
                                "longDescription": "Adjust the forming layout to match the new product requirements",
                                "timeRequired": 60
                            },
                            {
                                "name": "Change Sealing Roll",
                                "longDescription": "Replace or adjust the sealing roll for the new product",
                                "timeRequired": 45
                            },
                            {
                                "name": "Set manual parameters",
                                "longDescription": "Update machine settings according to product specifications",
                                "timeRequired": null
                            }
                        ]
                    },
                    {
                        "name": "Testing",
                        "description": "Run initials tests to confirm functionality",
                        "items": [
                            {
                                "name": "Check manual parameters",
                                "longDescription": "Verify that all manual settings are correct",
                                "timeRequired": 10
                            },
                            {
                                "name": "Dry run in 'mechanical test mode'",
                                "longDescription": "Perform a dry run to check mechanical operations",
                                "timeRequired": 15
                            },
                            {
                                "name": "Check sealing temperature",
                                "longDescription": "The temperature Ragne is between 185°C and 195°C",
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
                                "longDescription": "Verify that all steps in the checklist have been completed",
                                "timeRequired": 1
                            },
                            {
                                "name": "Digital signature",
                                "longDescription": "Sign off digitally to confirm the changeover process is complete",
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
                                "longDescription": "Guide the forming film to the unwinding section and secure the clamp",
                                "timeRequired": 40
                            },
                            {
                                "name": "Activate 'manual unwinding' on HMI",
                                "longDescription": "Start manual unwinding through the HMI",
                                "timeRequired": 1
                            },
                            {
                                "name": "Thread forming film to filling section",
                                "longDescription": "Direct the forming film to the filling section",
                                "timeRequired": 50
                            },
                            {
                                "name": "Clamp index after forming",
                                "longDescription": "Secure the film at the index after forming",
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
                                "longDescription": "Thread the lid foil to the sealed index",
                                "timeRequired": 30
                            },
                            {
                                "name": "Tape lid foil onto forming film",
                                "longDescription": "Attach the lid foil to the forming lid with tape",
                                "timeRequired": 2
                            },
                            {
                                "name": "Wind lid foil until pendulum is in working position",
                                "longDescription": "Wind the lid foil until the pendulum is in its working position",
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
                                "longDescription": "Insert leaflets into the designated area",
                                "timeRequired": 10
                            },
                            {
                                "name": "Insert Cartons",
                                "longDescription": "Insert cartons into the machine ",
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
                                "longDescription": "Close all doors and hatches for safety",
                                "timeRequired": 2
                            },
                            {
                                "name": "Set machine to 'ready to produce'",
                                "longDescription": "Switch the machine to 'ready to produce' mode",
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
                                "longDescription": "Confirm the machine is ready for production",
                                "timeRequired": 1
                            }
                        ]
                    }
                ]
            }
        ]
    }
]
                  
